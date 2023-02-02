package com.spring.customermanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.spring.customermanagementservice.*")
public class CustomerManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerManagementServiceApplication.class, args);
	}

}
