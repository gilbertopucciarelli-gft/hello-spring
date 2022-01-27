package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void rootTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/", String.class))
				.isEqualTo("This is the root path!");
	}

	@Test
	void helloTestDefault(@Autowired TestRestTemplate restTemplate) {
		String defaultValue = "World";
		assertThat(restTemplate.getForObject("/hello", String.class))
				.isEqualTo("Hello %s!", defaultValue);
	}

	@Test
	void helloTestVariable(@Autowired TestRestTemplate restTemplate) {
		String variableValue = "Gilberto";
		assertThat(restTemplate.getForObject(String.format("/hello?name=%s", variableValue), String.class))
				.isEqualTo("Hello %s!", variableValue);
	}
}
