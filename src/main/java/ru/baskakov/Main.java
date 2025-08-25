package ru.baskakov;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Product> products = createProductsList();
        List<Order> orders = createOrdersList(products);
        List<Customer> customers = createCustomersList(orders);
    }

    public static List<Product> createProductsList() {
        return List.of(
                new Product(1L, "iPhone 15 Pro", "Electronics", new BigDecimal("999.99")),
                new Product(2L, "Samsung Galaxy S24", "Electronics", new BigDecimal("899.99")),
                new Product(3L, "MacBook Air M2", "Electronics", new BigDecimal("1299.99")),
                new Product(4L, "iPad Air", "Electronics", new BigDecimal("599.99")),
                new Product(5L, "Clean Code", "Books", new BigDecimal("39.99")),
                new Product(6L, "Effective Java", "Books", new BigDecimal("44.99")),
                new Product(7L, "Design Patterns", "Books", new BigDecimal("49.99")),
                new Product(8L, "Organic Bananas", "Groceries", new BigDecimal("2.49")),
                new Product(9L, "Whole Milk", "Groceries", new BigDecimal("3.99")),
                new Product(10L, "Fresh Salmon", "Groceries", new BigDecimal("18.99")),
                new Product(11L, "Cotton T-Shirt", "Clothing", new BigDecimal("19.99")),
                new Product(12L, "Jeans", "Clothing", new BigDecimal("59.99")),
                new Product(13L, "Running Shoes", "Clothing", new BigDecimal("89.99")),
                new Product(14L, "Coffee Maker", "Home", new BigDecimal("79.99")),
                new Product(15L, "Blender", "Home", new BigDecimal("49.99")),
                new Product(16L, "Headphones", "Electronics", new BigDecimal("199.99")),
                new Product(17L, "Yoga Mat", "Sports", new BigDecimal("29.99")),
                new Product(18L, "Basketball", "Sports", new BigDecimal("24.99")),
                new Product(19L, "Chocolate", "Groceries", new BigDecimal("4.99")),
                new Product(20L, "Wine", "Groceries", new BigDecimal("25.99"))
        );
    }

    public static List<Order> createOrdersList(List<Product> productsList) {
        return List.of(
                createOrder(1L, productsList, Set.of(1, 2, 3, 4), 10),
                createOrder(2L, productsList, Set.of(5, 6, 7, 8, 9), 8),
                createOrder(3L, productsList, Set.of(10, 11, 12, 13, 14, 15), 7),
                createOrder(4L, productsList, Set.of(16), 6),
                createOrder(5L, productsList, Set.of(17, 18), 5),
                createOrder(6L, productsList, Set.of(19), 12),
                createOrder(7L, productsList, Set.of(20), 11),
                createOrder(8L, productsList, Set.of(20, 19), 14),
                createOrder(9L, productsList, Set.of(18, 17, 16), 4),
                createOrder(10L, productsList, Set.of(15, 14, 13, 12), 3),
                createOrder(11L, productsList, Set.of(11, 10, 9, 8, 7), 21),
                createOrder(12L, productsList, Set.of(6, 5, 4, 3, 2, 1), 3),
                createOrder(13L, productsList, Set.of(10), 1),
                createOrder(14L, productsList, Set.of(20), 2),
                createOrder(15L, productsList, Set.of(5, 10, 15, 20), 9),
                createOrder(16L, productsList, Set.of(1, 3, 5, 7), 13),
                createOrder(17L, productsList, Set.of(20, 18, 16), 15)
        );
    }

    public static List<Customer> createCustomersList(List<Order> ordersList) {
        return List.of(
                createCustomer(1L, "Ivan Ivanov", 1L, ordersList, Set.of(1, 2, 3, 4, 5)),
                createCustomer(2L, "Sergey Sergey", 2L, ordersList, Set.of(6, 7, 8, 9, 10)),
                createCustomer(3L, "Petr Petrov", 3L, ordersList, Set.of(11, 12, 13, 14, 15)),
                createCustomer(4L, "Alexey Alexeyev", 3L, ordersList, Set.of(16, 17, 1, 2, 3)),
                createCustomer(5L, "Machail Michailov", 2L, ordersList, Set.of(15, 14, 13, 12, 11)),
                createCustomer(6L, "Igor Igorev", 1L, ordersList, Set.of(10, 9, 8, 7, 6))
        );
    }

    public static Order createOrder(Long id, List<Product> products, Set<Integer> productIds,
                                    int daysAgoOrderDate) {
        Set<Product> orderInProduct = productIds.stream()
                .map(idx -> products.get(idx - 1))
                .collect(Collectors.toSet());

        LocalDate orderDate = LocalDate.now().plusDays(daysAgoOrderDate);
        LocalDate deliveryDate = orderDate.plusDays(ThreadLocalRandom.current().nextInt(3, 21));
        String orderStatus = generateOrderStatus();

        return new Order(id, orderDate, deliveryDate, orderStatus, orderInProduct);
    }

    public static Customer createCustomer(Long id, String name, Long level,
                                          List<Order> orders, Set<Integer> orderIds) {
        Set<Order> customerOrders = orderIds.stream()
                .map(idx -> orders.get(idx - 1))
                .collect(Collectors.toSet());

        return new Customer(id, name, level, customerOrders);
    }

    public static String generateOrderStatus() {
        double rnd = ThreadLocalRandom.current().nextDouble();
        if (rnd < 0.4) return "Delivered";
        if (rnd < 0.7) return "Shipped";
        if (rnd < 0.85) return "Processing";
        if (rnd < 0.95) return "Pending";

        return "Cancelled";
    }
}