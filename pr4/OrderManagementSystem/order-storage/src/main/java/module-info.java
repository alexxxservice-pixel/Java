module order.storage {
    requires order.processing;

    // Експортуємо публічний пакет сховища замовлень.
    // Внутрішній пакет com.example.order.storage.internal прихований від зовнішніх модулів.
    exports com.example.order.storage;
}
