package com.example.aflfa.service.inter;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface GifService {

    ResponseEntity<Map> getGif(String tag);
}
