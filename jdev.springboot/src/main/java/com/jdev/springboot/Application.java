package com.jdev.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Application implements WebMvcConfigurer {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

		/*
		 * BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); String result =
		 * encoder.encode("123"); System.out.println(result);
		 */

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/login").setViewName("/login");
		registry.setOrder(Ordered.LOWEST_PRECEDENCE);
	}

}
