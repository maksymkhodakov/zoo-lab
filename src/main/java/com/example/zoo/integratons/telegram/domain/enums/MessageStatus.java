package com.example.zoo.integratons.telegram.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageStatus {
    WELCOME_MESSAGE("Dear user! Welcome to the ZooKeeper Bot! This bot can give you basic info regarding zoo!"),

    HELP_MESSAGE("Dear user! To get link of this bot please use /link command. To get all zoo info use /zoo command."),

    DEFAULT_MESSAGE("Incorrect input! Please verify available commands via /help command."),

    SUCCESS_MESSAGE("Success response!"),

    ERROR_MESSAGE("Error occurred during response!"),

    NO_CONTENT("No content in API answer");

    private final String message;
}
