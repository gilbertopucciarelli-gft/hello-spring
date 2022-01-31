package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/")
	public String root() {
		return "This is the root path!";
	}

	@GetMapping("/add")
	public Object add(@RequestParam(value = "a", defaultValue = "0") Float a,
					  @RequestParam(value = "b", defaultValue = "0") Float b) {
		float sum = a+b;
		float decimals = sum - (int) sum;
		if(decimals != 0){
			return sum;
		}

		return (int) sum;
	}

	@GetMapping("/multiply")
	public Object multiply(@RequestParam(value = "a", defaultValue = "0") Float a,
						   @RequestParam(value = "b", defaultValue = "0") Float b) {
		float product = a*b;
		float decimals = product - (int) product;
		if(decimals != 0){
			return product;
		}

		return (int) product;
	}

	@GetMapping("/subtraction")
	public Object subtraction(@RequestParam(value = "a", defaultValue = "0") Float a,
							  @RequestParam(value = "b", defaultValue = "0") Float b) {
		float difference = a-b;
		float decimals = difference - (int) difference;
		if(decimals != 0){
			return difference;
		}

		return (int) difference;
	}

	@GetMapping("/divide")
	public Object divide(@RequestParam(value = "a", defaultValue = "0") Float a,
						 @RequestParam(value = "b", defaultValue = "0") Float b) {
		return a/b;
	}
}
