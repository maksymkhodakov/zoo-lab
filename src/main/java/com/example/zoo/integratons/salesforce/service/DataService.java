package com.example.zoo.integratons.salesforce.service;

import com.example.zoo.integratons.salesforce.domail.LoginResponse;

public interface DataService {
    <T> T process(final LoginResponse loginResponse, final String query, final Class<T> response);
}
