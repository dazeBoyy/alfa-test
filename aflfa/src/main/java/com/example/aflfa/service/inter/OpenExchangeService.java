package com.example.aflfa.service.inter;

import java.util.List;

public interface OpenExchangeService {

    List<String> getCharCodes();

    int getKeyForTag(String charCode);

    void refreshRates();
}
