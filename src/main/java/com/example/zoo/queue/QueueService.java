package com.example.zoo.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.List;

public interface QueueService {

    @Getter
    @AllArgsConstructor
    class Message<T> {
        private T payload;
        private String messageId;
        private String popReceipt;
    }

    void send(Object payload);

    <T> List<Message<T>> receive(int batchSize, Duration visibleTimeout, Class<T> clazz);

    void delete(Message<?> message);

}
