package com.example.zoo.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureStorageConfig {
    @Value("${azure.storage.connection.string}")
    private String connectionString;

    @Value("${azure.storage.blob.storage}")
    private String storageName;

    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }

    @Bean
    @Qualifier("photos")
    public BlobContainerClient blobContainerClient() {
        return blobServiceClient().getBlobContainerClient(storageName);
    }
}
