package com.example.aflfa.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "gif", url = "${giphy.url.general}")
public interface FeignGif {

    @GetMapping("/random")
    ResponseEntity<Map> getRandomGif(@RequestParam("api_key") String apiKey, @RequestParam("tag") String tag);
}
