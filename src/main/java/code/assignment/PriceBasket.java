package code.assignment;

import code.assignment.model.Product;
import code.assignment.service.CheckoutService;
import code.assignment.service.ReceiptService;
import code.assignment.service.StockService;
import code.assignment.service.impl.BasketServiceImpl;
import code.assignment.service.impl.CheckoutServiceImpl;
import code.assignment.service.impl.StockServiceImpl;

import java.util.List;
import java.util.Map;

import static code.assignment.util.Constants.PROPERTIES_FILE;
import static code.assignment.util.Constants.PROPERTY_NAME;

/**
 * Start application class.
 */

public class PriceBasket {

    public static void main(String[] items) {

        // start checkout
        Map<String, Object> checkoutResult = process(items);

        // receipt process and print
        String receipt = ReceiptService.prepareToPrint(checkoutResult);
        System.out.println(receipt);

    }

    /**
     * This is main process method that loads all services necessary for
     * basket calculation and checkout.
     *
     * @param items list of items to be buy
     * @return calculation output with offers messages
     */
    static Map<String, Object> process(String[] items) {
        StockService stockService = new StockServiceImpl();
        String stockEndpoint = stockService.getStockEndpoint(PROPERTIES_FILE, PROPERTY_NAME);
        List<Product> productList = stockService.createStock(stockEndpoint).getProductList();

        // create new basket with items
        Map<Product, Integer> basket = new BasketServiceImpl(items, productList).createNewBasket();

        // checkout process
        CheckoutService checkoutService = new CheckoutServiceImpl(basket, productList);
        return checkoutService.checkout();

    }

}

