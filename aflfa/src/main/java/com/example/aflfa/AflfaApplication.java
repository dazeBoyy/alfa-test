package com.example.aflfa;

import com.example.aflfa.feign.FeignOpenExchangeRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AflfaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AflfaApplication.class, args);
	}


}
