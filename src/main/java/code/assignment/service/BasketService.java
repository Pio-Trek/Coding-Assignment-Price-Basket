package code.assignment.service;

import code.assignment.model.Product;

import java.util.Map;

public interface BasketService {

    Map<Product, Integer> createNewBasket();

}