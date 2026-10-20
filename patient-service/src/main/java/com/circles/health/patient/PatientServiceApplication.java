package com.circles.health.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.circles.health.patient.email.CircleHealthEmail;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class PatientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientServiceApplication.class, args);
	}

	@Bean
	public CircleHealthEmail getCircleHealthEmail() {
		return new CircleHealthEmail();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
