package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");

        List<Product> catalog = new ArrayList<>();
        catalog.add(new Product(1, "Ноутбук", 19999.99, "Високопродуктивний ноутбук для роботи та ігор", electronics));
        catalog.add(new Product(2, "Смартфон", 12999.50, "Смартфон з великим екраном та високою автономністю", smartphones));
        catalog.add(new Product(3, "Навушники", 2499.00, "Бездротові навушники з шумозаглушенням", accessories));

        Cart cart = new Cart();
        List<Order> orderHistory = new ArrayList<>();

        while (true) {
            System.out.println("\n--- Меню інтернет-магазину ---");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Видалити товар з кошика");
            System.out.println("4 - Переглянути кошик");
            System.out.println("5 - Зробити замовлення");
            System.out.println("6 - Переглянути історію замовлень");
            System.out.println("7 - Пошук товарів за назвою або категорією");
            System.out.println("0 - Вийти");
            System.out.print("Виберіть дію: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть коректне числове значення пункту меню.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\nКаталог товарів:");
                    for (Product product : catalog) {
                        System.out.println(product);
                    }
                    break;

                case 2:
                    System.out.print("Введіть ID товару для додавання: ");
                    try {
                        int addId = Integer.parseInt(scanner.nextLine());
                        Product targetProduct = null;
                        for (Product product : catalog) {
                            if (product.getId() == addId) {
                                targetProduct = product;
                                break;
                            }
                        }
                        if (targetProduct != null) {
                            cart.addProduct(targetProduct);
                            System.out.println("Товар '" + targetProduct.getName() + "' додано до вашого кошика.");
                        } else {
                            System.out.println("Товар з ID " + addId + " не знайдено.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Некоректний формат ID.");
                    }
                    break;

                case 3:
                    System.out.print("Введіть ID товару для видалення з кошика: ");
                    try {
                        int removeId = Integer.parseInt(scanner.nextLine());
                        boolean isRemoved = cart.removeProductById(removeId);
                        if (isRemoved) {
                            System.out.println("Товар з ID " + removeId + " видалено з кошика.");
                        } else {
                            System.out.println("Товар з ID " + removeId + " відсутній у кошику.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Некоректний формат ID.");
                    }
                    break;

                case 4:
                    System.out.println("\n" + cart);
                    break;

                case 5:
                    if (cart.getProducts().isEmpty()) {
                        System.out.println("Неможливо оформити замовлення: кошик порожній.");
                    } else {
                        Order order = new Order(cart);
                        orderHistory.add(order);
                        System.out.println("\nЗамовлення успішно створено!");
                        System.out.println(order);
                        cart.clear();
                    }
                    break;

                case 6:
                    System.out.println("\nІсторія замовлень:");
                    if (orderHistory.isEmpty()) {
                        System.out.println("Ви ще не зробили жодного замовлення.");
                    } else {
                        for (int i = 0; i < orderHistory.size(); i++) {
                            System.out.println("№" + (i + 1) + " " + orderHistory.get(i));
                            System.out.println("----------------------------------------");
                        }
                    }
                    break;

                case 7:
                    System.out.print("Введіть пошуковий запит: ");
                    String query = scanner.nextLine().trim().toLowerCase();
                    boolean foundAny = false;
                    System.out.println("\nРезультати пошуку:");
                    for (Product product : catalog) {
                        boolean byName = product.getName().toLowerCase().contains(query);
                        boolean byCategory = product.getCategory() != null &&
                                product.getCategory().getName().toLowerCase().contains(query);

                        if (byName || byCategory) {
                            System.out.println(product);
                            foundAny = true;
                        }
                    }
                    if (!foundAny) {
                        System.out.println("За запитом '" + query + "' збігів не виявлено.");
                    }
                    break;

                case 0:
                    System.out.println("Роботу завершено. Дякуємо за покупки!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Невідомий пункт меню. Оберіть число від 0 до 7.");
                    break;
            }
        }
    }
}