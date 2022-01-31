package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DemoApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Nested
	class RootTests {

		@Test
		void rootTest() {
			assertThat(restTemplate.getForObject("/", String.class))
					.isEqualTo("This is the root path!");
		}
	}

	@Nested
	class HelloTests {

		@ParameterizedTest
		@ValueSource(strings = {"World", "Gilberto", "Gilberto%20Pucciarelli", "José%20Gregorio%20Hernández"})
		void helloParameterizedTest(String name) {
			assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
					.isEqualTo("Hello "+name+"!");
		}

		@DisplayName("/hello - Test Multiple Inputs")
		@ParameterizedTest(name="[{index}] ({arguments}) \"{0}\" -> \"{1}\"")
		@CsvSource({
				"a, 						  Hello a!",
				"null, 						  Hello null!",
				"'', 						  Hello World!",
				"' ', 						  Hello  !",
				"World, 					  Hello World!",
				"Gilberto, 					  Hello Gilberto!",
				"José Gregorio Hernández, 	  Hello José Gregorio Hernández!"

		})
		void helloParamsNamesCsv(String name, String expected) {
			assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
					.isEqualTo(expected);
		}
	}

	@Nested
	class AdditionTests {

		@ParameterizedTest()
		@CsvSource({
				"0, 0,	   0",  // Default Add Test
				"1, 2, 	   3",  // Basic Add Test
				"0, 10,    10", // Zero Add Test
				"10, -5,   5",  // Negative Integer Add Test
				"'', 10,   10", // A is null Add Test
				"10, '',   10", // B is null Add Test
				"2.5, 3.5, 6",  // Float Add Test
		})
		void addParamsCsv(String a, String b, String expected) {
			assertThat(restTemplate.getForObject("/add?a="+a+"&b="+b, String.class))
					.isEqualTo(expected);
		}

		@Test
		void canAddExceptionJsonString() {
			assertThat(restTemplate.getForObject("/add?a=string&b=1", String.class).indexOf("Bad Request"))
					.isGreaterThan(-1);
		}

		@Test
		void canAddFloat() {
			float a = 1.5f;
			float b = 2f;
			assertThat(restTemplate.getForObject("/add?a="+a+"&b="+b, Float.class))
					.isEqualTo(a+b);
		}

		@Test
		void canAddFloatException() {
			Exception thrown = assertThrows(RestClientException.class, () -> {
				restTemplate.getForObject("/add?a=string&b=2", Float.class);
			});
		}
	}

	@Nested
	class MultiplicationTests {

	}

	@Nested
	@DisplayName(value="Application tests")
	class AppTests {

		@Autowired
		private DemoApplication app;

		@Test
		void appCanAddReturnsInteger() {
			assertThat(app.add(1f, 2f)).isEqualTo(3);
		}

		@Test
		void appCanAddReturnsFloat() {
			assertThat(app.add(1.5f, 2f)).isEqualTo(3.5f);
		}

		@Test
		void appCanAddNull() {
			Exception thrown = assertThrows(NullPointerException.class, ()-> {
				Float ret = (Float) app.add(null, 2f);
			});

			assertTrue(thrown.toString().contains("NullPointerException"));
		}
	}
}
