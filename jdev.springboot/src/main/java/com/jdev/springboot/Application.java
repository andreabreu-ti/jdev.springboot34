package com.jdev.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		
		/*
		 * BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		 * String result = encoder.encode("123");
		 * System.out.println(result);
		 * */
	}

}
