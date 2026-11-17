package com.example.order.processing.internal;

import com.example.order.processing.Clothing;
import com.example.order.processing.Electronics;
import com.example.order.processing.InvalidOrderException;
import com.example.order.processing.Product;

/**
 * Внутрішній клас валідації та розрахунку вартості замовлення.
 * Цей пакет (com.example.order.processing.internal) НЕ експортується у module-info.java,
 * тому він недоступний з інших модулів (демонстрація модульного контролю доступу).
 */
public final class OrderValidator {

    private OrderValidator() {
    }

    /**
     * Перевіряє коректність узагальненого об'єкта товару <T extends Product>.
     * Кидає InvalidOrderException, якщо дані некоректні.
     */
    public static <T extends Product> void validate(T product) {
        if (product == null) {
            throw new InvalidOrderException("Об'єкт товару не може бути null!", "Unknown<T>");
        }
        String typeName = product.getClass().getSimpleName();
        if (product.getName() == null || product.getName().isBlank()) {
            throw new InvalidOrderException("Назва товару не може бути порожньою!", typeName);
        }
        if (product.getPrice() <= 0) {
            throw new InvalidOrderException(
                    "Ціна товару має бути більшою за 0 (отримано: " + product.getPrice() + ")",
                    typeName);
        }
        if (product instanceof Electronics electronics && electronics.getWarrantyMonths() < 0) {
            throw new InvalidOrderException(
                    "Термін гарантії електроніки не може бути від'ємним!",
                    typeName);
        }
        if (product instanceof Clothing clothing && (clothing.getSize() == null || clothing.getSize().isBlank())) {
            throw new InvalidOrderException(
                    "Розмір одягу обов'язковий для заповнення!",
                    typeName);
        }
    }

    /**
     * Внутрішній метод розрахунку податку/доставки залежно від типу товару.
     */
    public static <T extends Product> double calculateTotalWithFee(T product) {
        validate(product);
        if (product instanceof Electronics) {
            // Страховий збір 5% для електроніки
            return product.getPrice() * 1.05;
        } else if (product instanceof Clothing) {
            // Стандартний сервісний збір 2% для одягу
            return product.getPrice() * 1.02;
        }
        return product.getPrice();
    }
}
