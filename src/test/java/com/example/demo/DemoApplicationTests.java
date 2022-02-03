package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.RoundingMode.HALF_DOWN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DemoApplicationTests {

	public static final String PARAM_A = "a=";
	public static final String PARAM_B = "&b=";
	public static final String EXCEPTION_BAD_REQUEST = "Bad Request";
	public static final String EXCEPTION_NULL_POINTER = "NullPointerException";
	@Autowired
	transient TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Nested
	@DisplayName(value="/root Path Tests")
	class RootTests {

		@Test
		void rootTest() {
			assertThat(restTemplate.getForObject("/", String.class))
					.isEqualTo("This is the root path!");
		}
	}

	@Nested
	@DisplayName(value="/hello Path Tests")
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
	@DisplayName(value="Addition Tests")
	class AdditionTests {

		@ParameterizedTest(name="[{index}] - \"{0}\" + \"{1}\" -> \"{2}\"")
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
			assertThat(restTemplate.getForObject("/add?" + PARAM_A +a+ PARAM_B +b, String.class))
					.isEqualTo(expected);
		}

		@Test
		void canAddExceptionJsonString() {
			assertThat(restTemplate.getForObject("/add?" + PARAM_A + "string" + PARAM_B + "1", String.class).indexOf(EXCEPTION_BAD_REQUEST))
					.isGreaterThan(-1);
		}

		@Test
		void canAddFloat() {
			float a = 1.5f;
			float b = 2f;
			assertThat(restTemplate.getForObject("/add?" + PARAM_A +a+ PARAM_B +b, Float.class))
					.isEqualTo(a+b);
		}

		@Test
		void canAddFloatException() {
			assertThrows(RestClientException.class, () ->
					restTemplate.getForObject("/add?" + PARAM_A + "string" + PARAM_B + "2", Float.class));
		}
	}

	@Nested
	@DisplayName(value="Multiplication Tests")
	class MultiplicationTests {

		@ParameterizedTest(name="[{index}] - \"{0}\" * \"{1}\" -> \"{2}\"")
		@CsvSource({
				"0, 0, 0",       // Default Multiply Test
				"5, 5, 25",      // Basic Multiply Test
				"5, 0, 0",   	 // Zero Multiply Test
				"0, 5, 0",       // Zero Multiply Test
				"-10, -50, 500", // Negative Multiply Test
				"10, -10, -100", // Negative Multiply Test
				"'', 5, 0",		 // A is null Multiply Test
				"5, '', 0",		 // B is null Multiply Test
				"1.0, 1.0, 1",	 // Float Multiply Test
				"1.5, 1.5, 2.25" // Float Multiply Test
		})
		void canMultiply(String a, String b, String expected) {
			assertThat(restTemplate.getForObject("/multiply?" + PARAM_A +a+ PARAM_B +b, String.class))
					.isEqualTo(expected);
		}

		@Test
		void canMultiplyExceptionJsonString() {
			assertThat(restTemplate.getForObject("/multiply?" + PARAM_A + "string" + PARAM_B + "1", String.class).indexOf(EXCEPTION_BAD_REQUEST))
					.isGreaterThan(-1);
		}

		@Test
		void canMultiplyFloat() {
			float a = 1.5f;
			float b = 1.5f;
			assertThat(restTemplate.getForObject("/multiply?" + PARAM_A +a+ PARAM_B +b, Float.class))
					.isEqualTo(a*b);
		}

		@Test
		void canMultiplyFloatException() {
			assertThrows(RestClientException.class, () ->
					restTemplate.getForObject("/multiply?" + PARAM_A + "string" + PARAM_B + "2", Float.class));
		}
	}

	@Nested
	@DisplayName(value="Subtraction Tests")
	class SubtractionTests {

		@ParameterizedTest(name="[{index}] - \"{0}\" - \"{1}\" -> \"{2}\"")
		@CsvSource({
				"0, 0,	    0",   // Default Subtraction Test
				"2, 1, 	    1",   // Basic Subtraction Test
				"10, 0,     10",  // Zero Subtraction Test
				"0, 10,    -10",  // Zero Subtraction Test
				"'', 10,   -10",  // A is null Subtraction Test
				"10, '',    10",  // B is null Subtraction Test
				"-10, 5,   -15",  // A is Negative Subtraction Test
				"5, -10,    15",  // B is Negative Subtraction Test
				"-5, -5,     0",  // Both A&B Negative Subtraction Test
				"5, 1.75,  3.25", // Float Subtraction Test
				"2.5, -3.5,   6", // Float Negative Subtraction Test
				"-2.5, 3.5,  -6", // Float Negative Subtraction Test
		})
		void subtractionParamsCsv(String a, String b, String expected) {
			assertThat(restTemplate.getForObject("/subtraction?" + PARAM_A +a+ PARAM_B +b, String.class))
					.isEqualTo(expected);
		}

		@Test
		void canSubtractionExceptionJsonString() {
			assertThat(restTemplate.getForObject("/subtraction?" + PARAM_A + "string" + PARAM_B + "1", String.class).indexOf(EXCEPTION_BAD_REQUEST))
					.isGreaterThan(-1);
		}

		@Test
		void canSubtractionFloat() {
			float a = 1.5f;
			float b = 2f;
			assertThat(restTemplate.getForObject("/subtraction?" + PARAM_A +a+ PARAM_B +b, Float.class))
					.isEqualTo(a-b);
		}

		@Test
		void canSubtractionFloatException() {
			assertThrows(RestClientException.class, () ->
					restTemplate.getForObject("/subtraction?" + PARAM_A + "string" + PARAM_B + "2", Float.class));
		}
	}

	@Nested
	@DisplayName(value="Division Tests")
	class DivisionTests {

		@ParameterizedTest(name="[{index}] - \"{0}\" / \"{1}\" -> \"{2}\"")
		@CsvSource({
				"2, 2,      1.00",  // Same A&B Divide Test
				"4, 2,      2.00",  // Basic Divide Test
				"1, 2,      0.50",  // Float Result Divide Test
				"-10, 5,   -2.00",  // A is Negative Divide Test
				"20, -5,   -4.00",  // B is Negative Divide Test
				"-10, -10,  1.00",  // A&B is Negative Divide Test
				"5, 2,      2.50",  // Result is Float Test
				"1.5, 2,    0.75",  // A and Result are Float Test
				"0, 10,     0.00",  // A is Zero Divide Test
				"0.5, 0.5,  1.00",  // Both A&B are Float Divide Test
				"0.5, 0.75, 0.67",  // Both A&B are Float Divide Test

		})
		void divideParamsCsv(String a, String b, String expected) {
			assertThat(restTemplate.getForObject("/divide?" + PARAM_A +a+ PARAM_B +b, String.class))
					.isEqualTo(expected);
		}

		@Test
		void divideByZero() {
			assertThrows(RestClientException.class, () ->
					restTemplate.getForObject("/divide?" + PARAM_A + "10" + PARAM_B + "0", Float.class));
		}

		@Test
		void canDivideExceptionJsonString() {
			assertThat(restTemplate.getForObject("/divide?" + PARAM_A + "string" + PARAM_B + "1", String.class).indexOf(EXCEPTION_BAD_REQUEST))
					.isGreaterThan(-1);
		}

		@Test
		void canDivideFloat() {
			float a = 1.5f;
			float b = 2f;
			assertThat(restTemplate.getForObject("/divide?" + PARAM_A +a+ PARAM_B +b, Float.class))
					.isEqualTo(a/b);
		}

		@Test
		void canDivideFloatException() {
			assertThrows(RestClientException.class, () ->
					restTemplate.getForObject("/divide?" + PARAM_A + "string" + PARAM_B + "2", Float.class));
		}
	}

	@Nested
	@DisplayName(value="Square Root Tests")
	class SqrtTests {

		@ParameterizedTest(name="[{index}] - \"{0}\" -> \"{1}\"")
		@CsvSource({
				"0,       0", // Default sqrt Test
				"4,       2", // Basic sqrt Test
				"16,      4", // Basic sqrt Test
				"36,      6", // Basic sqrt Test
				"64,      8", // Basic sqrt Test
				"100,    10", // Basic sqrt Test
				"0.5, 0.707", // Float sqrt Test
				"3.14, 1.77", // Float sqrt Test
		})
		void sqrtParamsCsv(String base, String expected) {
			assertThat(restTemplate.getForObject("/sqrt?base="+base, String.class))
					.isEqualTo(expected);
		}

		@Test
		void sqrtNegativeBase() {
			assertThrows(RestClientException.class, () ->
					restTemplate.getForObject("/sqrt?base=-1", BigDecimal.class));
		}

		@Test
		void canSqrtExceptionJsonString() {
			assertThat(restTemplate.getForObject("/sqrt?base=string", String.class).indexOf(EXCEPTION_BAD_REQUEST))
					.isGreaterThan(-1);
		}

		@Test
		void canSqrtFloat() {
			float base = 0.5f;
			assertThat(restTemplate.getForObject("/sqrt?base="+base, Float.class))
					.isEqualTo(0.707f);
		}
	}

	@Nested
	@DisplayName(value="Application Tests")
	class AppTests {

		@Autowired
		private transient DemoApplication app;

		// Addition Tests

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
			Exception thrown = assertThrows(NullPointerException.class, ()->
					app.add(null, 2f));
			assertTrue(thrown.toString().contains(EXCEPTION_NULL_POINTER));
		}

		// Multiply Tests

		@Test
		void appCanMultiplyReturnsInteger() {
			assertThat(app.multiply(1f, 2f)).isEqualTo(2);
		}

		@Test
		void appCanMultiplyReturnsFloat() {
			assertThat(app.multiply(1.5f, 1.5f)).isEqualTo(2.25f);
		}

		@Test
		void appCanMultiplyNull() {
			Exception thrown = assertThrows(NullPointerException.class, ()->
					app.multiply(null, 2f));
			assertTrue(thrown.toString().contains(EXCEPTION_NULL_POINTER));
		}

		// Subtraction Tests

		@Test
		void appCanSubtractReturnsInteger() {
			assertThat(app.subtraction(2f, 1f)).isEqualTo(1);
		}

		@Test
		void appCanSubtractReturnsFloat() {
			assertThat(app.subtraction(2f, 1.5f)).isEqualTo(0.5f);
		}

		@Test
		void appCanSubtractNull() {
			Exception thrown = assertThrows(NullPointerException.class, ()->
					app.subtraction(null, 2f));
			assertTrue(thrown.toString().contains(EXCEPTION_NULL_POINTER));
		}

		// Division Tests

		@Test
		void appCanDivideReturnsInteger() {
			BigDecimal a = new BigDecimal(4);
			BigDecimal b = new BigDecimal(2);
			assertThat(app.divide(a, b)).isEqualTo(a.divide(b, 2, HALF_DOWN));
		}

		@Test
		void appCanDivideReturnsFloat() {
			BigDecimal a = new BigDecimal("0.5");
			BigDecimal b = new BigDecimal("0.75");
			assertThat(app.divide(a, b)).isEqualTo(a.divide(b, 2, HALF_DOWN));
		}

		@Test
		void appCanDivideNull() {
			Exception thrown = assertThrows(NullPointerException.class, ()->
					app.divide(null, new BigDecimal(10)));
			assertTrue(thrown.toString().contains(EXCEPTION_NULL_POINTER));
		}

		// Sqrt Tests

		@Test
		void appCanSqrtReturnsInteger() {
			BigDecimal base = new BigDecimal(81);
			assertThat(app.sqrt(base)).isEqualTo(base.sqrt(new MathContext(3, HALF_DOWN)));
		}

		@Test
		void appCanSqrtReturnsFloat() {
			BigDecimal base = new BigDecimal(11);
			assertThat(app.sqrt(base)).isEqualTo(base.sqrt(new MathContext(3, HALF_DOWN)));
		}

		@Test
		void appCanSqrtNull() {
			Exception thrown = assertThrows(NullPointerException.class, ()->
					app.sqrt(null));
			assertTrue(thrown.toString().contains(EXCEPTION_NULL_POINTER));
		}
	}
}
