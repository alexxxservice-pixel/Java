package com.example.order.processing;

import lombok.Builder;
import lombok.Getter;

/**
 * Клас товару категорії "Електроніка", що успадковує базовий клас Product.
 * Використовує патерн Builder за допомогою анотації Lombok @Builder.
 */
@Getter
public class Electronics extends Product {
    private final String brand;
    private final int warrantyMonths;

    @Builder
    public Electronics(String id, String name, double price, String brand, int warrantyMonths) {
        super(id, name, price);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getCategory() {
        return "Електроніка";
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Бренд: %s, Гарантія: %d міс.", brand, warrantyMonths);
    }
}
