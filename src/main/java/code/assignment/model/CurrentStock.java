package code.assignment.model;

import java.util.List;

/**
 * Model class used to create CurrentStock object.
 */

public class CurrentStock {

    private final List<Product> productList;

    public CurrentStock(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
