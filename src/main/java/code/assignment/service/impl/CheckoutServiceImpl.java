package code.assignment.service.impl;

import code.assignment.exception.OfferNotFoundException;
import code.assignment.model.Product;
import code.assignment.model.Promotion;
import code.assignment.service.CheckoutService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static code.assignment.service.OfferService.buy2GetProductForHalfPrice;
import static code.assignment.service.OfferService.discountInPercentOff;
import static code.assignment.util.Constants.*;

/**
 * This service layer class is responsible for calculation of the checkout process.
 */

public class CheckoutServiceImpl implements CheckoutService {

    private Map<Product, Integer> basket;
    private List<Product> productList;

    public CheckoutServiceImpl(Map<Product, Integer> basket, List<Product> productList) {
        this.basket = basket;
        this.productList = productList;
    }

    /**
     * This method executes a checkout taking into account any available special
     * offers and discounts and creates subtotal, total price and offers massage.
     *
     * @return HashMap with subtotal, total price and special offers message
     */
    public Map<String, Object> checkout() {
        BigDecimal subtotal = new BigDecimal("0.00");
        BigDecimal totalDiscount = new BigDecimal("0.00");
        StringBuilder discountText = new StringBuilder();

        Map<String, String> offerResults;

        for (Map.Entry<Product, Integer> entry : basket.entrySet()) {
            Product product = entry.getKey();

            int quantity = entry.getValue();
            Promotion promotion = product.getPromotion();

            // calculate subtotal: product_price * quantity
            subtotal = subtotal.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            // check if product in under any promotion
            if (promotion != null) {
                switch (promotion.getOfferName().toUpperCase().trim()) {
                    case "BUY_2_GET_PRODUCT_FOR_HALF_PRICE":
                        offerResults = buy2GetProductForHalfPrice(productList, product, quantity);
                        break;
                    case "DISCOUNT":
                        offerResults = discountInPercentOff(product, quantity);
                        break;
                    default:
                        throw new OfferNotFoundException("[ Unable to found offer: " + promotion.getOfferName() + " ]");
                }

                // add extra product total price from special offer to subtotal
                if (offerResults.containsKey(SUBTOTAL_KEY)) {
                    subtotal = subtotal.add(new BigDecimal(offerResults.get(SUBTOTAL_KEY)));
                }

                // add product discount to total discount
                if (offerResults.containsKey(DISCOUNT_KEY)) {
                    totalDiscount = totalDiscount.add(new BigDecimal(offerResults.get(DISCOUNT_KEY)));
                }

                // append special offers texts
                if (offerResults.containsKey(OFFER_TEXT_KEY)) {
                    discountText.append(offerResults.get(OFFER_TEXT_KEY));
                }
            }

        }

        // create output keys and values
        Map<String, Object> checkoutResult = new HashMap<>();
        checkoutResult.put(SUBTOTAL_KEY, subtotal);
        checkoutResult.put(DISCOUNT_KEY, totalDiscount);
        checkoutResult.put(OFFER_TEXT_KEY, discountText);

        return checkoutResult;
    }

}
