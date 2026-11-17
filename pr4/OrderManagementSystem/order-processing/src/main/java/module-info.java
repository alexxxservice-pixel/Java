module order.processing {
    requires java.base;
    requires static lombok;

    // Експортуємо лише публічний API модуля обробки замовлень.
    // Пакет com.example.order.processing.internal НЕ експортується,
    // що забезпечує модульний контроль доступу (інкапсуляцію внутрішніх класів).
    exports com.example.order.processing;
}
