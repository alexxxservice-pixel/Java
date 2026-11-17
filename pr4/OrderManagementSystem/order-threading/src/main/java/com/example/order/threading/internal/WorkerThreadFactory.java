package com.example.order.threading.internal;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Внутрішня фабрика потоків для пулу обробки замовлень.
 * Розміщена у неекспортованому пакеті com.example.order.threading.internal,
 * що обмежує доступ до неї з інших модулів.
 */
public class WorkerThreadFactory implements ThreadFactory {
    private final AtomicInteger counter = new AtomicInteger(1);
    private final String prefix;

    public WorkerThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, prefix + "-" + counter.getAndIncrement());
        thread.setUncaughtExceptionHandler((t, e) ->
                System.err.printf("[Помилка потоку %s]: %s%n", t.getName(), e.getMessage()));
        return thread;
    }
}
