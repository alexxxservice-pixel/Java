package com.example.order.threading;

import com.example.order.processing.InvalidOrderException;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.Product;
import com.example.order.threading.internal.WorkerThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Клас управління багатопотоковою обробкою узагальнених замовлень.
 * Демонструє використання лямбда-виразів для створення потоків, пулу потоків ExecutorService
 * та посилань на методи (Method References) для пакетної обробки.
 */
public class OrderThreadManager {
    private final int poolSize;

    public OrderThreadManager(int poolSize) {
        this.poolSize = poolSize;
    }

    /**
     * Обробляє одне замовлення в окремому потоці за допомогою лямбда-виразу Runnable
     * (відповідно до прикладу з підказки завдання).
     */
    public <T extends Product> void processInSingleThread(OrderProcessor<T> orderProcessor) {
        Runnable orderTask = () -> {
            System.out.println("Processing order in thread " + Thread.currentThread().getName());
            orderProcessor.process();
        };
        Thread thread = new Thread(orderTask, "LambdaOrderThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InvalidOrderException("Потік обробки замовлення було перервано!",
                    orderProcessor.getProduct().getCategory(), e);
        }
    }

    /**
     * Паралельно обробляє список узагальнених замовлень у пулі потоків за допомогою лямбда-виразів.
     */
    public <T extends Product> void processOrdersConcurrently(List<OrderProcessor<T>> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            throw new InvalidOrderException("Список замовлень для багатопотокової обробки порожній!", "List<T>");
        }

        ExecutorService executor = Executors.newFixedThreadPool(
                poolSize, new WorkerThreadFactory("OrderWorker"));
        List<Future<?>> futures = new ArrayList<>();

        try {
            for (OrderProcessor<T> processor : orderList) {
                // Використовуємо лямбда-вираз для подачі завдання в пул потоків
                Future<?> future = executor.submit(() -> {
                    System.out.println("Processing order in thread " + Thread.currentThread().getName());
                    processor.process();
                });
                futures.add(future);
            }

            // Очікуємо завершення всіх потоків та перевіряємо наявність виключень
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    System.err.println("Перехоплено виключення під час багатопотокової обробки: " + cause.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Виконує обробку списку замовлень за допомогою посилання на метод (Method Reference):
     * orderList.forEach(OrderProcessor::processOrder);
     */
    public <T extends Product> void processWithMethodReference(List<OrderProcessor<T>> orderList) {
        if (orderList == null) {
            throw new InvalidOrderException("Список замовлень не може бути null!", "List<T>");
        }
        orderList.forEach(OrderProcessor::processOrder);
    }
}
