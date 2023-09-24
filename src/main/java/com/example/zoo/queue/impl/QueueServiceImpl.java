package com.example.zoo.queue.impl;

import com.azure.core.util.Context;
import com.azure.storage.queue.QueueClient;
import com.example.zoo.exceptions.ApiErrors;
import com.example.zoo.exceptions.OperationException;
import com.example.zoo.queue.QueueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private final QueueClient queueClient;
    private final ObjectMapper objectMapper;

    @Override
    public void send(Object payload) {
        try {
            final String payloadMessage = objectMapper.writeValueAsString(payload);
            queueClient.sendMessage(payloadMessage);
        } catch (JsonProcessingException e) {
            log.error("Error occurred during json serialization");
        }
    }

    @Override
    public <T> List<Message<T>> receive(int batchSize, Duration visibleTimeout, Class<T> clazz) {
        return queueClient.receiveMessages(batchSize, visibleTimeout, null, Context.NONE)
                .stream()
                .peek(message -> log.info("Message received with id " + message.getMessageId()))
                .map(message -> new Message<>(readJson(message.getMessageText(), clazz), message.getMessageId(), message.getPopReceipt()))
                .toList();
    }

    private <T> T readJson(String body, Class<T> clazz) {
        try {
            return objectMapper.readValue(body, clazz);
        } catch (JsonProcessingException e) {
            throw new OperationException(ApiErrors.ERROR_OCCURRED_JSON_ERROR);
        }
    }

    @Override
    public void delete(Message<?> message) {
        log.info("Message with id : " + message.getMessageId() + " removed from queue");
        queueClient.deleteMessage(message.getMessageId(), message.getPopReceipt());
    }
}
