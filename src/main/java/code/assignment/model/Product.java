package code.assignment.model;

import java.math.BigDecimal;

/**
 * Model class used to create Product object.
 */

public class Product {

    private final long id;
    private final String name;
    private final BigDecimal price;
    private final Promotion promotion;

    public Product(long id, String name, BigDecimal price, Promotion promotion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.promotion = promotion;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
