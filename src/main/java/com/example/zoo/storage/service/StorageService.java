package com.example.zoo.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    byte[] downloadPhoto(String path);
    String uploadPhoto(MultipartFile multipartFile);
    void deletePhoto(String path);
}
