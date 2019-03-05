package code.assignment.service.impl;

import code.assignment.model.CurrentStock;
import code.assignment.model.Product;
import code.assignment.service.StockService;
import code.assignment.util.FileReader;
import code.assignment.util.PropertiesReader;
import com.google.gson.Gson;

/**
 * This service layer class is responsible for loading a stock file from a source
 */

public class StockServiceImpl implements StockService {

    /**
     * This method uses {@link PropertiesReader} to get a stock source path and
     * {@link FileReader} to save a file content as a String.
     *
     * @param propertiesFile properties file name
     * @param propertyName   property with stock source path
     * @return current stock in JSON as a String
     */
    public String getStockEndpoint(String propertiesFile, String propertyName) {
        PropertiesReader properties = new PropertiesReader();

        String stockFileName = properties.getStockFileName(propertiesFile, propertyName);

        return FileReader.get(stockFileName);
    }

    /**
     * This method evokes {@link Gson} deserialize function to create
     * a {@link CurrentStock} object with list of {@link Product}
     *
     * @param jsonCurrentStock String with current stock in JSON format
     * @return current stock object
     */
    public CurrentStock createStock(String jsonCurrentStock) {
        return new Gson().fromJson(jsonCurrentStock, CurrentStock.class);
    }


}
