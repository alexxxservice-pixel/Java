package com.example.order.threading;

import com.example.order.processing.Clothing;
import com.example.order.processing.Electronics;
import com.github.javafaker.Faker;

import java.util.Locale;

/**
 * Генератор тестових товарів, який використовує бібліотеку JavaFaker
 * для наповнення об'єктів та патерн Builder (Lombok @Builder) для їх створення.
 */
public class FakeProductGenerator {
    private final Faker faker;

    public FakeProductGenerator() {
        // Використовуємо фіксований або стандартний Faker для генерації реалістичних даних
        this.faker = new Faker(Locale.ENGLISH);
    }

    /**
     * Створює об'єкт Electronics за допомогою Lombok @Builder та наповнює його даними з JavaFaker.
     */
    public Electronics generateElectronics() {
        double price = Double.parseDouble(
                faker.commerce().price(150.0, 2500.0).replace(",", "."));
        int[] warranties = {12, 24, 36};
        int warranty = warranties[faker.random().nextInt(warranties.length)];

        return Electronics.builder()
                .id("EL-" + faker.number().digits(4))
                .name(faker.commerce().productName())
                .brand(faker.company().name())
                .price(price)
                .warrantyMonths(warranty)
                .build();
    }

    /**
     * Створює об'єкт Clothing за допомогою Lombok @Builder та наповнює його даними з JavaFaker.
     */
    public Clothing generateClothing() {
        double price = Double.parseDouble(
                faker.commerce().price(25.0, 450.0).replace(",", "."));
        String[] sizes = {"XS", "S", "M", "L", "XL"};
        String size = sizes[faker.random().nextInt(sizes.length)];

        return Clothing.builder()
                .id("CL-" + faker.number().digits(4))
                .name(faker.commerce().productName())
                .material(faker.commerce().material())
                .size(size)
                .price(price)
                .build();
    }
}
