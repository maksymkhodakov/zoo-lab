package com.example.zoo.integratons.telegram.service;

import com.example.zoo.integratons.telegram.domain.dto.ZooTelegramDTO;
import com.example.zoo.integratons.telegram.domain.entities.TelegramUserLogs;
import com.example.zoo.integratons.telegram.domain.enums.MessageStatus;
import com.example.zoo.integratons.telegram.repository.TelegramLogsRepository;
import com.example.zoo.mapper.ZooMapper;
import com.example.zoo.repository.ZooRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Objects;

import static com.example.zoo.integratons.telegram.domain.enums.MessageStatus.*;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot {
    private final List<BotCommand> botCommands;
    private final TelegramLogsRepository telegramLogsRepository;
    private final ZooRepository zooRepository;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("${telegram.bot.token}")
    private String token;

    @PostConstruct
    private void setCommandsToMyBot() {
        try {
            execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Telegram API error occurred: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            final var message = update.getMessage();
            final var text = message.getText();
            switch (text) {
                case "/start" -> answer(message, WELCOME_MESSAGE.getMessage());
                case "/help" -> answer(message, HELP_MESSAGE.getMessage());
                case "/link" -> answer(message, "t.me/" + name);
                case "/zoo" -> getZoo(message);
                default -> answer(message, DEFAULT_MESSAGE.getMessage());
            }
        }
    }

    private void getZoo(Message message) {
        final List<ZooTelegramDTO> payload = zooRepository
                .findAll()
                .stream()
                .map(ZooMapper::entityToTelegramDTO)
                .toList();
        String answer = getAnswerFromPayload(payload);
        answer(message, answer);
    }

    private String getAnswerFromPayload(List<ZooTelegramDTO> payload) {
        if (Objects.isNull(payload) || payload.isEmpty()) {
            return NO_CONTENT.getMessage();
        }
        final StringBuilder sb = new StringBuilder();
        payload.forEach(p -> {
            sb.append(generateMessage(p));
            sb.append("\n");
        });
        return sb.toString();
    }

    private String generateMessage(ZooTelegramDTO p) {
        return "\nID:" + p.getId() + " \nNAME:" + p.getName() + "\nSQUARE:" + p.getSquare() + "\nCOORDINATES:"
                + p.getCoordinates() + "\nANIMAL NAMES:" + p.getAnimalNames() + "\n";
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void answer(Message message, String answer) {
        SendMessage sendMessage = new SendMessage(String.valueOf(message.getChatId()), answer);
        try {
            log.info("Message: " + answer + " \n Was sent to user: " + message.getChat().getUserName());
            execute(sendMessage);
            saveLogs(message, SUCCESS_MESSAGE.getMessage());
        } catch (TelegramApiException e) {
            saveLogs(message, ERROR_MESSAGE.getMessage());
        }
    }

    public void saveLogs(Message message, String response) {
        var logData = TelegramUserLogs.builder()
                .chatId(message.getChatId())
                .firstName(message.getChat().getFirstName())
                .lastName(message.getChat().getLastName())
                .username(message.getChat().getUserName())
                .request(message.getText())
                .response(response)
                .build();
        final var logSaved = telegramLogsRepository.saveAndFlush(logData);
        log.info(String.format("Telegram Log with id: %s was created", logSaved.getId()));
    }

}
