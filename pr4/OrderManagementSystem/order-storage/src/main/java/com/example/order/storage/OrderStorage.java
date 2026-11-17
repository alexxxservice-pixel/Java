package com.example.order.storage;

import com.example.order.processing.InvalidOrderException;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.Product;
import com.example.order.storage.internal.InMemoryStorageEngine;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Узагальнене сховище замовлень з обмеженням типу <T extends Product>.
 * Підтримує потокобезпечне додавання, отримання та фільтрацію замовлень за допомогою лямбда-виразів.
 */
public class OrderStorage<T extends Product> {
    private final InMemoryStorageEngine<T> engine = new InMemoryStorageEngine<>();

    /**
     * Додає нове замовлення у сховище.
     */
    public void addOrder(OrderProcessor<T> order) {
        if (order == null || order.getProduct() == null) {
            throw new InvalidOrderException("Неможливо зберегти порожнє замовлення у сховищі!", "NullOrder");
        }
        engine.save(order);
    }

    /**
     * Створює та зберігає OrderProcessor для переданого товару T.
     */
    public OrderProcessor<T> addProductOrder(T product) {
        OrderProcessor<T> processor = new OrderProcessor<>(product);
        engine.save(processor);
        return processor;
    }

    /**
     * Повертає всі замовлення зі сховища.
     */
    public List<OrderProcessor<T>> getAllOrders() {
        return engine.findAll();
    }

    /**
     * Фільтрує замовлення за допомогою переданого лямбда-виразу (Predicate<T>).
     */
    public List<OrderProcessor<T>> filterOrders(Predicate<T> predicate) {
        if (predicate == null) {
            throw new InvalidOrderException("Умова фільтрації (Predicate) не може бути null!", "FilterPredicate");
        }
        return engine.findAll().stream()
                .filter(order -> predicate.test(order.getProduct()))
                .collect(Collectors.toList());
    }

    /**
     * Узагальнений метод фільтрації замовлень за конкретним підтипом товару <S extends T>
     * із безпечним приведенням узагальненого типу та обробкою виключень.
     */
    public <S extends T> List<OrderProcessor<S>> filterByType(Class<S> typeClass) {
        if (typeClass == null) {
            throw new InvalidOrderException("Клас типу для фільтрації не може бути null!", "Class<S>");
        }
        return engine.findAll().stream()
                .filter(order -> typeClass.isInstance(order.getProduct()))
                .map(order -> {
                    try {
                        S castProduct = typeClass.cast(order.getProduct());
                        return new OrderProcessor<>(castProduct);
                    } catch (ClassCastException e) {
                        throw new InvalidOrderException(
                                "Помилка приведення узагальненого типу до " + typeClass.getSimpleName(),
                                typeClass.getSimpleName(),
                                e);
                    }
                })
                .collect(Collectors.toList());
    }

    public int size() {
        return engine.count();
    }
}
