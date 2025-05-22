package com.ankur.product_service;

import com.ankur.product_service.controller.ProductController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		Arrays.stream(ProductController.class.getConstructors())
						.forEach(System.out::println);
		SpringApplication.run(ProductServiceApplication.class, args);

	}

}
