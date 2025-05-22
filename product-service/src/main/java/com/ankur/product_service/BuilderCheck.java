package com.ankur.product_service;

import com.ankur.product_service.model.Product;

import java.math.BigDecimal;

public class BuilderCheck {
    public static void main(String[] args) {
        Product product = Product.builder()
                .name("Phone")
                .description("A nice phone")
                .price(new BigDecimal("799.99"))
                .build();

        System.out.println(product);
    }
}
