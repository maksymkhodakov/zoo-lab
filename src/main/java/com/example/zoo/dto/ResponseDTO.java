package com.example.zoo.dto;

import lombok.Getter;

/**
 * This class would be a wrapper for all REST API calls
 */
@Getter
public class ResponseDTO<T> {
    private T data;
    private ResponseStatus status;
    private String errorMessage;

    public static <T> ResponseDTO<T> ofData(T data, ResponseStatus status) {
        return new ResponseDTO<>(data, status);
    }

    public static ResponseDTO<Void> ok() {
        return new ResponseDTO<>(ResponseStatus.OK);
    }

    public static ResponseDTO<Void> error(String errorMessage) {
        return new ResponseDTO<>(ResponseStatus.ERROR, errorMessage);
    }

    public enum ResponseStatus {
        OK,
        ERROR
    }

    public ResponseDTO(ResponseStatus status) {
        this.status = status;
    }

    public ResponseDTO(T data, ResponseStatus status) {
        this.data = data;
        this.status = status;
    }

    public ResponseDTO(ResponseStatus responseStatus, String errorMessage) {
        this.status = responseStatus;
        this.errorMessage = errorMessage;
    }
}
