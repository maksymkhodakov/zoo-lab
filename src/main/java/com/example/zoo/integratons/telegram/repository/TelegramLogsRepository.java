package com.example.zoo.integratons.telegram.repository;

import com.example.zoo.integratons.telegram.domain.entities.TelegramUserLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramLogsRepository extends JpaRepository<TelegramUserLogs, Long> {
}
