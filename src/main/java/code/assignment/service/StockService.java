package code.assignment.service;

import code.assignment.model.CurrentStock;

public interface StockService {

    String getStockEndpoint(String propertiesFile, String propertyName);

    CurrentStock createStock(String jsonCurrentStock);

}
