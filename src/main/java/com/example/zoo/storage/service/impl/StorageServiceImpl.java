package com.example.zoo.storage.service.impl;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private static final String PHOTO_FOLDER = "photos/";
    private final BlobContainerClient blobContainerClient;

    @Override
    public byte[] downloadPhoto(String path) {
        if (Objects.isNull(path) || path.isEmpty()) {
            log.info("Blob path is empty");
            return null;
        }
        try (var outputStream = new ByteArrayOutputStream()) {
            var blobClient = blobContainerClient.getBlobClient(path);
            if (Boolean.FALSE.equals(blobClient.exists())) {
                log.info("Blob not exists");
                return null;
            }
            blobClient.download(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Error occurred during file downloading");
            throw new OperationException(ApiErrors.DOWNLOAD_FILE_FAILED);
        }
    }

    @Override
    public String uploadPhoto(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            return null;
        }
        var blobName = PHOTO_FOLDER + UUID.randomUUID();
        var blobClient = blobContainerClient.getBlobClient(blobName).getBlockBlobClient();
        upload(multipartFile, blobClient);
        return blobName;
    }

    private void upload(MultipartFile multipartFile, BlockBlobClient blobClient) {
        try (BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream())) {
            blobClient.uploadWithResponse(
                    bis,
                    multipartFile.getSize(),
                    new BlobHttpHeaders().setContentType(multipartFile.getContentType()),
                    null,
                    null,
                    null,
                    new BlobRequestConditions(),
                    null,
                    Context.NONE
            );
        } catch (IOException e) {
            log.error("Error occurred during file upload to the storage");
            throw new OperationException(ApiErrors.UPLOAD_FILE_ERROR);
        }
    }

    @Override
    public void deletePhoto(String path) {
        if (Objects.isNull(path) || path.isEmpty()) {
            log.info("Blob is empty");
            return;
        }
        var blobClient = blobContainerClient.getBlobClient(path).getBlockBlobClient();
        if (Boolean.TRUE.equals(blobClient.exists())) {
            blobClient.delete();
        } else {
            log.info("No blob exists by this path");
        }
    }
}
