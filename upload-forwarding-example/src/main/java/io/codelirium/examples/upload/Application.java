package io.codelirium.examples.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { Application.BASE_PACKAGE })
public class Application {

	public static final String BASE_PACKAGE = "io.codelirium.examples.upload";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestTemplate restClient() {
		return new RestTemplate();
	}
}
