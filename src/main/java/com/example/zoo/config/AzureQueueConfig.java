package com.example.zoo.config;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureQueueConfig {
    @Value("${azure.storage.connection.string}")
    private String connectionString;

    @Value("${azure.storage.queue.name}")
    private String queueName;

    @Bean
    public QueueClient taskQueue() {
        return new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(queueName)
                .buildClient();
    }
}
