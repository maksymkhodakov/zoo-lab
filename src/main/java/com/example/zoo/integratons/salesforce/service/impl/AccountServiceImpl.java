package com.example.zoo.integratons.salesforce.service.impl;

import com.example.zoo.integratons.salesforce.domail.LoginResponse;
import com.example.zoo.integratons.salesforce.service.AccountService;
import com.example.zoo.integratons.salesforce.service.AuthService;
import com.example.zoo.integratons.salesforce.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AuthService authService;
    private final DataService dataService;

    /**
     * NOTE!
     * Salesforce Object Query Language (SOQL) does not support * syntax, so you need to specify exact column you want
     * See @see <a href="https://developer.salesforce.com/docs/atlas.en-us.object_reference.meta/object_reference/sforce_api_objects_account.htm">Account</a>
     * to see ALL!!!! Account fields
     */
    private static final String ALL_ACCOUNTS = "SELECT Id, Name FROM Account";
    private static final String ACCOUNT_BY_NAME = "SELECT Id, Name, AccountNumber FROM Account WHERE Name = '%s'";

    @Override
    public Map getAccounts() {
        final LoginResponse loginResponse = authService.login();
        return dataService.process(loginResponse, ALL_ACCOUNTS, Map.class);
    }

    @Override
    public Map getAccountByName(String name) {
        final String query = String.format(ACCOUNT_BY_NAME, name);
        final LoginResponse loginResponse = authService.login();
        return dataService.process(loginResponse, query, Map.class);
    }
}
