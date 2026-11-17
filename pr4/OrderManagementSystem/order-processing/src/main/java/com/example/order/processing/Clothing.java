package com.example.order.processing;

import lombok.Builder;
import lombok.Getter;

/**
 * Клас товару категорії "Одяг", що успадковує базовий клас Product.
 * Використовує патерн Builder за допомогою анотації Lombok @Builder.
 */
@Getter
public class Clothing extends Product {
    private final String size;
    private final String material;

    @Builder
    public Clothing(String id, String name, double price, String size, String material) {
        super(id, name, price);
        this.size = size;
        this.material = material;
    }

    @Override
    public String getCategory() {
        return "Одяг";
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Розмір: %s, Матеріал: %s", size, material);
    }
}
