package code.assignment.service.impl;

import code.assignment.exception.ProductNotFoundException;
import code.assignment.model.Product;
import code.assignment.service.BasketService;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This service layer class is responsible for creating a basket with
 * products and quantity.
 */

public class BasketServiceImpl implements BasketService {

    private String[] items;
    private List<Product> productList;

    public BasketServiceImpl(String[] items, List<Product> productList) {
        this.items = items;
        this.productList = productList;
    }

    /**
     * This method iterates a current stock product list and compares
     * with array of items to create a basket.
     *
     * @return HashMap with {@link Product} as a key and quantity as a value
     */
    public Map<Product, Integer> createNewBasket() {
        Map<Product, Integer> basket = new HashMap<>();

        validateInputIsNotEmpty();

        System.out.println("\n**** YOUR BASKET ****");

        // iterate product list and compare with items to create a basket
        for (Product product : productList) {

            validateProductPrice(product);

            if (StringUtils.isNotEmpty(product.getName())) {
                Integer quantity = Math.toIntExact(Arrays.stream(items)
                        .filter(i -> i.equalsIgnoreCase(product.getName()))
                        .count());
                if (quantity > 0) {
                    basket.put(product, quantity);
                    System.out.println(quantity + " x " + product.getName());
                }
            } else {
                throw new IllegalArgumentException("[ One or more product name in current stock are null. Please check the stock file. ]");
            }
        }

        validateItemName();

        System.out.println("*********************\n");

        return basket;
    }


    /**
     * This method validates if the array of items is not empty (no app
     * arguments) and throws an code.assignment.exception.
     */
    private void validateInputIsNotEmpty() {
        if (items.length <= 0) {
            StringBuilder availableProducts = new StringBuilder();
            for (Product product : productList) {
                availableProducts.append(product.getName()).append(" ");
            }
            throw new IllegalArgumentException("[ Items input cannot be empty. Available products: " + availableProducts + "]");
        }
    }

    /**
     * This method validates if the item name is listed in current stock and
     * throws an code.assignment.exception.
     */
    private void validateItemName() {
        for (String item : items) {
            productList.stream().filter(p -> p.getName().equalsIgnoreCase(item)).findFirst().orElseThrow(
                    () -> new ProductNotFoundException("[ Product not exists in the current stock: " + item + " ]"));
        }
    }

    /**
     * This method validates if the product price is grater then zero.
     *
     * @param product product object for validation
     */
    private void validateProductPrice(Product product) {
        BigDecimal productPrice = product.getPrice();
        if (productPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ Product (" + product.getName() + ") price must be a positive value ]");
        }
    }

}