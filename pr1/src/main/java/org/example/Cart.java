package org.example;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Product> products;

    public Cart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProductById(int id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public void clear() {
        products.clear();
    }

    @Override
    public String toString() {
        if (products.isEmpty()) {
            return "Кошик порожній.";
        }
        StringBuilder sb = new StringBuilder("Кошик містить:\n");
        for (Product product : products) {
            sb.append(product.toString()).append("\n");
        }
        sb.append("Загальна вартість: ").append(getTotalPrice());
        return sb.toString();
    }
}