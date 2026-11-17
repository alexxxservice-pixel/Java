package com.example.order.processing;

/**
 * Спеціалізоване виключення для обробки помилок під час роботи з узагальненими замовленнями
 * (наприклад, некоректні дані товару, від'ємна ціна, порожня назва або null-об'єкт).
 */
public class InvalidOrderException extends RuntimeException {
    private final String productType;

    public InvalidOrderException(String message, String productType) {
        super(message);
        this.productType = productType;
    }

    public InvalidOrderException(String message, String productType, Throwable cause) {
        super(message, cause);
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    @Override
    public String getMessage() {
        return String.format("[Тип товару: %s] %s", productType, super.getMessage());
    }
}
