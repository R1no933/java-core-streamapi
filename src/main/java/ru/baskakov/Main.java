package ru.baskakov;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
    private static final String SPLITTER = "==================================================================";

    public static void main(String[] args) {
        List<Product> products = createProductsList();
        List<Order> orders = createOrdersList(products);
        List<Customer> customers = createCustomersList(orders);

        /*Задание 1. Получите список продуктов из категории "Books" с ценой более 40.
         * Изменил 100 на 40, так как в ТЗ нет четких правил инициализации данных, по этому
         * нет ни одного товара из категории "Books" с ценой больше 100. */

        List<Product> booksList = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .flatMap(order -> order.getProducts().stream())
                .filter(prdCat -> prdCat.getCategory().equals("Books")
                        && prdCat.getPrice().compareTo(new BigDecimal(40)) > 0)
                .collect(Collectors.toList());
        System.out.println("Вывод задания № 1:");
        booksList.forEach(System.out::println);
        System.out.println(SPLITTER);

        /*Задание 2. Получите список заказов с продуктами из категории "Clothing".
         * Заменил "Children's products" на "Clothing" так как в ТЗ нет четких правил инициализации данных, по этому
         * нет ни одного товара из категории "Children's products"*/
        List<Order> clothingList = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .filter(order -> order.getProducts().stream()
                        .anyMatch(prdCat -> prdCat.getCategory().equals("Clothing")))
                .collect(Collectors.toList());
        System.out.println("Вывод задания № 2:");
        clothingList.forEach(System.out::println);
        System.out.println(SPLITTER);

        /*Задание 3. Получите список продуктов из категории "Sports" и примените скидку 10% и получите сумму всех
         * продуктов.
         * Заменил "Toys" на "Sports"*/
        BigDecimal totalPriceWithDiscount = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .flatMap(order -> order.getProducts().stream())
                .filter(product -> product.getCategory().equals("Sports"))
                .map(product -> product.getPrice().multiply(new BigDecimal(0.9)).divide(new BigDecimal(1), 2, RoundingMode.FLOOR)
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Вывод задания № 3:");
        System.out.println("Общая цена товаров категории Sports со скидкой: " + totalPriceWithDiscount);
        System.out.println(SPLITTER);

        /*Задание 4. Получите список продуктов, заказанных клиентом второго уровня между 01-сент-2025 и 18-сент-2025.
         * Заменнил даты.*/
        LocalDate startDate = LocalDate.of(2025, 9, 1);
        LocalDate endDate = LocalDate.of(2025, 9, 18);
        List<Product> betweenDateProductList = customers.stream()
                .filter(customer -> customer.getLevel().equals(2L))
                .flatMap(customer -> customer.getOrders().stream())
                .filter(order -> order.getOrderDate().isAfter(startDate) && order.getOrderDate().isBefore(endDate))
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.toList());
        System.out.println("Вывод задания № 4:");
        betweenDateProductList.forEach(System.out::println);
        System.out.println(SPLITTER);

        /*Задание 5. Получите топ 2 самые дешевые продукты из категории "Books".*/
        List<Product> topCheapBooks = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .flatMap(order -> order.getProducts().stream())
                .filter(product -> product.getCategory().equals("Books"))
                .sorted(Comparator.comparing(Product::getPrice))
                .limit(2)
                .collect(Collectors.toList());
        System.out.println("Вывод задания № 5:");
        topCheapBooks.forEach(System.out::println);
        System.out.println(SPLITTER);

        /*Задание 6. Получите 3 самых последних сделанных заказа.*/
        List<Order> lastOrders = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .sorted(Comparator.comparing(Order::getOrderDate))
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("Вывод задания № 6:");
        lastOrders.forEach(System.out::println);
        System.out.println(SPLITTER);

        /*Задание 7. Получите список заказов, сделанных 28-августа-2025, выведите id заказов в консоль и затем верните
         * список их продуктов.
         * Заменил дату.*/
        LocalDate currentDate = LocalDate.of(2025, 8, 28);
        List<Order> nineteenSeptemberOrder = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .filter(order -> order.getOrderDate().isEqual(currentDate))
                .peek(order -> System.out.println("ID заказа(ов): " + order.getId()))
                .collect(Collectors.toList());
        System.out.println("Вывод задания № 7:");
        nineteenSeptemberOrder.forEach(System.out::println);
        System.out.println(SPLITTER);

        /*Задание 8. Рассчитайте общую сумму всех заказов, сделанных в августе 2025.
         * Заменил дату*/
        LocalDate start = LocalDate.of(2025, 8, 1);
        LocalDate end = LocalDate.of(2025, 8, 31);
        BigDecimal totalAugustPrice = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .filter(order -> order.getDeliveryDate().isAfter(start) || order.getDeliveryDate().isBefore(end))
                .flatMap(order -> order.getProducts().stream())
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Вывод задания № 8:");
        System.out.println("Общая сумма заказов в августе: " + totalAugustPrice);
        System.out.println(SPLITTER);

        /*Задание 9. Рассчитайте средний платеж по заказам, сделанным 02-сентября-2025.
         * Заменил дату*/
        LocalDate fourteenSeptember = LocalDate.of(2025, 9, 2);
        OptionalDouble average = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .filter(order -> order.getOrderDate().isEqual(fourteenSeptember))
                .flatMap(order -> order.getProducts().stream())
                .map(Product::getPrice)
                .mapToDouble(a -> a.doubleValue())
                .average();

        System.out.println("Вывод задания № 9:");
        System.out.println("Средняя цена заказов, сделанных 02-09-2025: " + (average.isPresent() ? average.getAsDouble() : "0"));
        System.out.println(SPLITTER);

        /*Задание 10. Получите набор статистических данных (сумма, среднее, максимум, минимум, количество) для всех
         * продуктов категории "Books".*/
        List<Product> bookList = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .flatMap(order -> order.getProducts().stream())
                .filter(product -> product.getCategory().equals("Books"))
                .collect(Collectors.toList());

        BigDecimal sumPriceBook = bookList.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal maxPriceOfBook = bookList.stream()
                .map(Product::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal minPriceOfBook = bookList.stream()
                .map(Product::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        OptionalDouble avgBookPrice = bookList.stream()
                .map(Product::getPrice)
                .mapToDouble(a -> a.doubleValue())
                .average();

        long countOfBooks = bookList.stream().count();

        System.out.println("Вывод задания № 10:");
        System.out.println("Общая сумма товаров категории Books: " + sumPriceBook);
        System.out.println("Средняя цена товаров категории Books: " + (avgBookPrice.isPresent() ? avgBookPrice.getAsDouble() : "0"));
        System.out.println("Максимальная цена товаров категории Books: " + maxPriceOfBook);
        System.out.println("Минимальная цена товаров категории Books: " + minPriceOfBook);
        System.out.println("Количество товаров категории Books: " + countOfBooks);
        System.out.println(SPLITTER);


        /*Задание 11. Получите данные Map<Long, Integer> → key - id заказа, value - кол-во товаров в заказе*/
        Map<Long, Integer> orderMapIdCount = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .collect(Collectors.toMap(Order::getId, order -> order.getProducts().size(),
                        (existing, replacement) -> existing));

        System.out.println("Вывод задания № 11:");
        for (Map.Entry<Long, Integer> entry : orderMapIdCount.entrySet()) {
            System.out.println("ID заказа: " + entry.getKey() + " - Количество товаров: " + entry.getValue());
        }
        System.out.println(SPLITTER);

        /*Задание 12. Создайте Map<Customer, List<Order>> → key - покупатель, value - список его заказов*/
        Map<Customer, List<Order>> customerAndOrderMap = customers.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        customer -> orders.stream()
                                .collect(Collectors.toList())
                ));

        System.out.println("Вывод задания № 12:");
        for (Map.Entry<Customer, List<Order>> entry : customerAndOrderMap.entrySet()) {
            System.out.println("Клиент: " + entry.getKey().toString() + " - Заказы: " + entry.getValue());
        }
        System.out.println(SPLITTER);

        /*Задание 13. Создайте Map<Order, Double> → key - заказ, value - общая сумма продуктов заказа.*/
        Map<Order, Double> orderAndPriceMap = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .collect(Collectors.toMap(
                        Function.identity(),
                        order -> order.getProducts().stream()
                                .map(Product::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue(),
                        (existing, replacement) -> existing
                ));

        System.out.println("Вывод задания № 13:");
        for (Map.Entry<Order, Double> entry : orderAndPriceMap.entrySet()) {
            System.out.println("ID заказа: " + entry.getKey().getId() + " - Сумма товаров в заказе: " + entry.getValue());
        }
        System.out.println(SPLITTER);

        /*Задание 14. Получите Map<String, List<String>> → key - категория, value - список названий товаров в категории*/
        Map<String, List<String>> categoryAndCountMap = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.mapping(Product::getName, Collectors.toList())
                ));

        System.out.println("Вывод задания № 14:");
        for (Map.Entry<String, List<String>> entry : categoryAndCountMap.entrySet()) {
            System.out.println("Категория товаров: " + entry.getKey() + " - Список наименований товаров: " + entry.getValue());
        }
        System.out.println(SPLITTER);

        /*Задание 15. Получите Map<String, Product> → самый дорогой продукт по каждой категории.*/
        Map<String, Product> expensiveProductMap = customers.stream()
                .flatMap(customer -> customer.getOrders().stream())
                .flatMap(order -> order.getProducts().stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Product::getPrice)),
                                Optional::get
                        )
                ));

        System.out.println("Вывод задания № 15:");
        for (Map.Entry<String, Product> entry : expensiveProductMap.entrySet()) {
            System.out.println("Категория товара: " + entry.getKey() + " - Самый дорогой товар в категории: " + entry.getValue().getName() + " - Цена: " + entry.getValue().getPrice());
        }
        System.out.println(SPLITTER);

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