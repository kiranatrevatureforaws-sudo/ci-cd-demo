package com.rev.app.service;

import com.rev.app.model.Product;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>(List.of(
            new Product(1L, "Laptop", new BigDecimal("75000.00"), 10, "Electronics"),
            new Product(2L, "Keyboard", new BigDecimal("1500.00"), 25, "Electronics"),
            new Product(3L, "Desk Chair", new BigDecimal("5500.00"), 15, "Furniture"),
            new Product(4L, "Notebook", new BigDecimal("80.00"), 100, "Stationery"),
            new Product(5L, "Water Bottle", new BigDecimal("350.00"), 50, "Kitchen")
    ));
    private long nextId = 6L;

    public synchronized List<Product> findAll() {
        return products.stream().map(this::copy).toList();
    }

    public synchronized Optional<Product> findById(Long id) {
        return products.stream().filter(product -> product.getId().equals(id))
                .findFirst().map(this::copy);
    }

    public synchronized Product create(Product product) {
        Product created = copy(product);
        created.setId(nextId++);
        products.add(created);
        return copy(created);
    }

    public synchronized Optional<Product> update(Long id, Product product) {
        for (int index = 0; index < products.size(); index++) {
            if (products.get(index).getId().equals(id)) {
                Product updated = copy(product);
                updated.setId(id);
                products.set(index, updated);
                return Optional.of(copy(updated));
            }
        }
        return Optional.empty();
    }

    public synchronized boolean delete(Long id) {
        return products.removeIf(product -> product.getId().equals(id));
    }

    private Product copy(Product product) {
        return new Product(product.getId(), product.getName(), product.getPrice(),
                product.getQty(), product.getCategory());
    }
}
