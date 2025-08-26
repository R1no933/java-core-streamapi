package ru.baskakov;

import java.math.BigDecimal;

public class Product {
    private final Long id;
    private final String name;
    private final String category;
    private final BigDecimal price;

    public Product(Long id, String name, String category, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("""
                                
                                Product ID: %s
                                Product Name: %s
                                Product Category: %s
                                Product Price: %s
                """, id, name, category, price);
    }
}
