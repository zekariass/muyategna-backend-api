package com.muyategna.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableRetry
public class MuyategnaBackendApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuyategnaBackendApiApplication.class, args);
	}

}
