package com.rohitmunde.trustdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class TrustDeskApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrustDeskApplication.class, args);
	}
}
