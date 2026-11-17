package com.example.order.processing;

import com.example.order.processing.internal.OrderValidator;

/**
 * Узагальнений клас обробки замовлень, де параметр типу T обмежений класом Product
 * (<T extends Product>). Це гарантує, що система працює лише з Product та його підтипами.
 *
 * @param <T> тип товару, що успадковує Product (наприклад, Electronics або Clothing)
 */
public class OrderProcessor<T extends Product> {
    private final T product;

    public OrderProcessor(T product) {
        // Використовуємо внутрішній неекспортований клас OrderValidator для валідації
        OrderValidator.validate(product);
        this.product = product;
    }

    public T getProduct() {
        return product;
    }

    /**
     * Основний метод обробки замовлення (відповідно до підказки завдання).
     */
    public void process() {
        OrderValidator.validate(product);
        double finalTotal = OrderValidator.calculateTotalWithFee(product);
        System.out.printf("[%s] Processing order for: %s | Категорія: %s | До сплати (з комісією): %.2f грн%n",
                Thread.currentThread().getName(),
                product.getName(),
                product.getCategory(),
                finalTotal);
    }

    /**
     * Статичний узагальнений метод для виклику через посилання на метод (Method Reference):
     * orderList.forEach(OrderProcessor::processOrder);
     */
    public static <T extends Product> void processOrder(OrderProcessor<T> orderProcessor) {
        if (orderProcessor == null) {
            throw new InvalidOrderException("OrderProcessor не може бути null під час пакетної обробки!", "NullProcessor");
        }
        orderProcessor.process();
    }

    /**
     * Додатковий статичний метод для виводу деталей товару через посилання на метод:
     * products.forEach(OrderProcessor::printProductDetails);
     */
    public static <T extends Product> void printProductDetails(T product) {
        OrderValidator.validate(product);
        System.out.println("  -> " + product);
    }

    @Override
    public String toString() {
        return "OrderProcessor{" + product + "}";
    }
}
