module order.threading {
    requires order.processing;
    requires order.storage;
    requires javafaker;
    requires java.sql;

    // Експортуємо публічний API модуля управління потоками.
    // Внутрішній пакет com.example.order.threading.internal прихований від зовнішніх модулів.
    exports com.example.order.threading;
}
