package com.example.aflfa.feign;

import com.example.aflfa.model.ExchangeRates;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "open-exchange", url = "${openexchangerates.url.general}")
public interface FeignOpenExchangeRates {

    @GetMapping("/latest.json")
    ExchangeRates getLatestRates(@RequestParam("app_id") String appId);

    @GetMapping("/historical/{date}.json")
    ExchangeRates getHistoricalRates(@PathVariable String date, @RequestParam("app_id") String appId);
}
