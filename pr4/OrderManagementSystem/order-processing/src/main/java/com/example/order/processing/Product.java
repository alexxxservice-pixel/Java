package com.example.order.processing;

/**
 * Базовий абстрактний клас товару інтернет-магазину.
 * Використовується як верхня межа узагальнення (bounded type parameter: <T extends Product>).
 */
public abstract class Product {
    private final String id;
    private final String name;
    private final double price;

    protected Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Повертає назву категорії товару.
     */
    public abstract String getCategory();

    /**
     * Повертає детальний опис специфічних характеристик підтипу товару.
     */
    public abstract String getSpecificDetails();

    @Override
    public String toString() {
        return String.format("[%s | ID: %s] %s — %.2f грн (%s)",
                getCategory(), id, name, price, getSpecificDetails());
    }
}
