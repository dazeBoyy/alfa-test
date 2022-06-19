package com.example.aflfa.service.Imp;

import com.example.aflfa.feign.FeignGif;
import com.example.aflfa.service.inter.GifService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GifServiceImp implements GifService {

    private FeignGif gifClient;
    @Value("${giphy.api.key}")
    private String apiKey;

    public GifServiceImp(FeignGif gifClient) {
        this.gifClient = gifClient;
    }

    @Override
    public ResponseEntity<Map> getGif(String tag) {
        ResponseEntity<Map> result = gifClient.getRandomGif(this.apiKey, tag);
        result.getBody().put("compareResult", tag);
        return result;
    }
}
