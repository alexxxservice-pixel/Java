package com.example.order.storage.internal;

import com.example.order.processing.OrderProcessor;
import com.example.order.processing.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Внутрішній потокобезпечний рушій збереження замовлень.
 * Розміщений у неекспортованому пакеті com.example.order.storage.internal,
 * тому інші модулі не мають до нього прямого доступу.
 */
public class InMemoryStorageEngine<T extends Product> {
    private final List<OrderProcessor<T>> orders = new CopyOnWriteArrayList<>();

    public void save(OrderProcessor<T> order) {
        orders.add(order);
    }

    public List<OrderProcessor<T>> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(orders));
    }

    public int count() {
        return orders.size();
    }
}
