package com.example.zoo.queue;

import com.example.zoo.entity.Animal;
import com.example.zoo.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalConsumer implements InitializingBean {

    @Value("${queue.batch-size}")
    private int batchSize;

    @Value("${queue.timeout}")
    private int timeout;

    @Value("${queue.pooling-timeout}")
    private int poolingTimeout;

    @Value("${queue.enabled}")
    private String queueEnabled;

    private final QueueService queueService;
    private final AnimalRepository animalRepository;

    @Override
    public void afterPropertiesSet() {
        if (Boolean.TRUE.equals(Boolean.parseBoolean(queueEnabled))) {
            CompletableFuture.runAsync(() -> {
                log.info("Queue listener has started");
                List<CompletableFuture<Void>> todos = new ArrayList<>();
                while (true) {
                    try {
                        ThreadUtils.sleep(Duration.ofMillis(poolingTimeout));
                        queueService.receive(batchSize, Duration.ofMinutes(timeout), Animal.class)
                                .forEach(animal -> todos.add(animalCreationTask(animal)));
                        CompletableFuture.allOf(todos.toArray(CompletableFuture[]::new))
                                .orTimeout(timeout, TimeUnit.MINUTES).join();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        log.info("Error occurred during animals consumption");
                    } finally {
                        todos.clear();
                    }
                }
            });
        }
    }

    private CompletableFuture<Void> animalCreationTask(QueueService.Message<Animal> message) {
        return CompletableFuture.runAsync(() -> animalRepository.saveAndFlush(message.getPayload()))
                .thenRun(() -> queueService.delete(message));
    }
}
