package com.example.SomeAmbitious;

import com.example.SomeAmbitious.configuration.BatchConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import(BatchConfiguration.class)
@SpringBootApplication(scanBasePackages = {"com.example.SomeAmbitious.configuration"})
@EnableScheduling
@Slf4j
public class AmbitiousApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmbitiousApplication.class, args);
		log.info("cgyhj");
	}
}
