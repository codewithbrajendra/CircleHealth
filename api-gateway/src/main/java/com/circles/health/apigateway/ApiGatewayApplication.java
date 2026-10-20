package com.circles.health.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/auth-service/**")
						.filters(f -> {
							f.setResponseHeader("Access-Control-Allow-Origin", "*");
							f.setResponseHeader("Access-Control-Allow-Headers", "*");
							return f;
						})
						//.uri("http://3.10.188.151:8990")) //QA Env
						//.uri("http://18.134.37.34:8990")) //Dev Env
						.uri("http://localhost:8990"))
						//.uri("http://35.179.84.204:8990")) //UAT Env
						//.uri("http://18.134.194.23:8990"))   //Prod Env
				.route(p -> p
						.path("/patient-service/**")
						.filters(f -> {
							f.setResponseHeader("Access-Control-Allow-Origin", "*");
							f.setResponseHeader("Access-Control-Allow-Headers", "*");
							return f;
						})
						//.uri("http://3.10.188.151:8991")) //QA Env
				        //.uri("http://18.134.37.34:8991")) //Dev Env
						.uri("http://localhost:8991"))
						//.uri("http://35.179.84.204:8991")) //UAT Env
						//.uri("http://18.134.194.23:8991"))   //Prod Env
				.route(p -> p
						.path("/doctor-service/**")
						.filters(f -> {
							f.setResponseHeader("Access-Control-Allow-Origin", "*");
							f.setResponseHeader("Access-Control-Allow-Headers", "*");
							return f;
						})
						//.uri("http://3.10.188.151:8992")) //QA Env
				        //.uri("http://18.134.37.34:8992")) //Dev Env
						.uri("http://localhost:8992"))
						//.uri("http://35.179.84.204:8992")) //UAT Env
						//.uri("http://18.134.194.23:8992"))  //Prod Env
				.build();
	}
}
