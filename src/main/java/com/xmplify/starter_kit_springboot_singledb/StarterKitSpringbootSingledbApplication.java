package com.xmplify.starter_kit_springboot_singledb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class StarterKitSpringbootSingledbApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(StarterKitSpringbootSingledbApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(StarterKitSpringbootSingledbApplication.class);
	}

}
