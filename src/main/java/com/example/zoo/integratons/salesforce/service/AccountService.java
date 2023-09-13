package com.example.zoo.integratons.salesforce.service;

import java.util.Map;

public interface AccountService {
    Map getAccounts();

    Map getAccountByName(String name);
}
