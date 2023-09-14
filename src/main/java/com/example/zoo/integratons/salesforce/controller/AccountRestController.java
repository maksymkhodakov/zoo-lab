package com.example.zoo.integratons.salesforce.controller;

import com.example.zoo.dto.ResponseDTO;
import com.example.zoo.integratons.salesforce.service.AccountService;
import com.microsoft.applicationinsights.TelemetryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/salesforce")
@RequiredArgsConstructor
public class AccountRestController {
    private final AccountService accountService;
    private final TelemetryClient telemetryClient = new TelemetryClient();

    @GetMapping(value = "/accounts")
    public ResponseDTO<Map> getAccounts() {
        telemetryClient.trackEvent("Get accounts from salesforce");
        try {
            return ResponseDTO.ofData(accountService.getAccounts(), ResponseDTO.ResponseStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDTO.ofData(Collections.emptyMap(), ResponseDTO.ResponseStatus.ERROR);
        }
    }

    @GetMapping(value = "/account")
    public ResponseDTO<Map> getAccountByName(@RequestParam String name) {
        try {
            return ResponseDTO.ofData(accountService.getAccountByName(name), ResponseDTO.ResponseStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseDTO.ofData(Collections.emptyMap(), ResponseDTO.ResponseStatus.ERROR);
        }
    }
}
