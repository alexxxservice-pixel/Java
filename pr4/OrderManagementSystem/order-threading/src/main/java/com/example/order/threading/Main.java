package com.example.order.threading;

import com.example.order.processing.Clothing;
import com.example.order.processing.Electronics;
import com.example.order.processing.InvalidOrderException;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.Product;
import com.example.order.storage.OrderStorage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Головний клас демонстрації багатопотокової системи управління замовленнями
 * з використанням узагальнень (Generics), Lombok @Builder, JavaFaker,
 * лямбда-виразів, посилань на методи та модульної архітектури Java (JPMS).
 */
public class Main {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        System.out.println("=== 1. Генерація товарів через JavaFaker та Lombok @Builder ===");
        FakeProductGenerator generator = new FakeProductGenerator();
        OrderStorage<Product> storage = new OrderStorage<>();

        // Створюємо 3 замовлення на електроніку та 3 замовлення на одяг
        for (int i = 0; i < 3; i++) {
            Electronics electronics = generator.generateElectronics();
            Clothing clothing = generator.generateClothing();
            storage.addProductOrder(electronics);
            storage.addProductOrder(clothing);
        }

        // Вивід згенерованих товарів за допомогою посилання на метод (Method Reference)
        storage.getAllOrders().stream()
                .map(OrderProcessor::getProduct)
                .forEach(OrderProcessor::printProductDetails);

        System.out.println("\n=== 2. Обробка замовлення в окремому потоці через лямбда-вираз ===");
        OrderThreadManager threadManager = new OrderThreadManager(3);
        OrderProcessor<Product> firstOrder = storage.getAllOrders().get(0);
        threadManager.processInSingleThread(firstOrder);

        System.out.println("\n=== 3. Багатопотокова обробка всіх замовлень у пулі потоків ===");
        threadManager.processOrdersConcurrently(storage.getAllOrders());

        System.out.println("\n=== 4. Фільтрація замовлень за типом (Generics + Lambdas) та Method Reference ===");
        List<OrderProcessor<Electronics>> electronicsOrders = storage.filterByType(Electronics.class);
        System.out.println("Відфільтровані замовлення категорії 'Електроніка' (" + electronicsOrders.size() + " шт.):");
        // Виклик через посилання на метод: orderList.forEach(OrderProcessor::processOrder)
        threadManager.processWithMethodReference(electronicsOrders);

        List<OrderProcessor<Product>> expensiveOrders = storage.filterOrders(p -> p.getPrice() > 300.0);
        System.out.println("\nФільтрація замовлень з ціною > 300 грн через лямбда-вираз (" + expensiveOrders.size() + " шт.):");
        expensiveOrders.forEach(OrderProcessor::processOrder);

        System.out.println("\n=== 5. Обробка виключень для узагальнених типів (<T extends Product>) ===");
        try {
            Electronics invalidElectronics = Electronics.builder()
                    .id("EL-ERR")
                    .name("Некоректний ноутбук")
                    .brand("TechBrand")
                    .price(-500.0)
                    .warrantyMonths(12)
                    .build();
            OrderProcessor<Electronics> invalidProcessor = new OrderProcessor<>(invalidElectronics);
            invalidProcessor.process();
        } catch (InvalidOrderException e) {
            System.out.println("Успішно перехоплено виключення: " + e.getMessage());
        }
    }
}
