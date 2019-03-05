package code.assignment.service;

import code.assignment.exception.ProductNotFoundException;
import code.assignment.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static code.assignment.util.Constants.*;

public interface OfferService {

    /**
     * This method calculates "Buy 2 products and get extra product for a half
     * price" special offer".
     *
     * @param productList current product stock list
     * @param product     product in special offer
     * @param quantity    product quantity
     * @return HashMap with subtotal, total price and special offer message
     */
    static Map<String, String> buy2GetProductForHalfPrice(List<Product> productList, Product product, int quantity) {
        Map<String, String> results = new HashMap<>();
        BigDecimal extraProductPrice = new BigDecimal("0");

        // check if minimum 2 products are in the basket
        for (int i = 2; i <= quantity; i += 2) {

            Long extraProductId = product.getPromotion().getExtraProductId();
            if (extraProductId != null) {

                // validate if extra product exists in the current product stock
                Product extraProduct = productList.stream()
                        .filter(p -> p.getId() == extraProductId).findFirst().orElseThrow(() -> new ProductNotFoundException("[ Current stock do not contains product with ID: " + extraProductId + " ]"));

                extraProductPrice = extraProductPrice.add(extraProduct.getPrice());

                results.put(OFFER_TEXT_KEY, String.format("%s 50%% off: -£%s%n", extraProduct.getName(), extraProductPrice.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP).toString()));
                results.put(SUBTOTAL_KEY, extraProductPrice.toString());
                results.put(DISCOUNT_KEY, extraProductPrice.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP).toString());

            } else {
                throw new IllegalArgumentException("[ Product included in the promotion cannot be null ]");
            }
        }
        return results;
    }

    /**
     * This method calculates special product offer discount.
     *
     * @param product  product in special offer
     * @param quantity product quantity
     * @return HashMap with total price and special offer message
     */
    static Map<String, String> discountInPercentOff(Product product, Integer quantity) {
        Map<String, String> results = new HashMap<>();
        BigDecimal discountValue = product.getPromotion().getDiscountValue();
        BigDecimal fixedPercentageOff;
        BigDecimal totalDiscountPerProduct;

        // check if discount value is in the allowed range (1%-99%)
        if (((discountValue.compareTo(MIN_DISCOUNT_VALUE) >= 0) && discountValue.compareTo(MAX_DISCOUNT_VALUE) <= 0)) {

            // calculate discount
            BigDecimal priceBeforeDiscount = product.getPrice();
            BigDecimal discountPerProduct = discountValue.multiply(priceBeforeDiscount);
            BigDecimal totalDiscount = discountPerProduct.multiply(BigDecimal.valueOf(quantity));
            fixedPercentageOff = discountValue.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_EVEN);
            totalDiscountPerProduct = discountPerProduct.multiply(BigDecimal.valueOf(quantity));

            results.put(DISCOUNT_KEY, totalDiscount.setScale(2, RoundingMode.CEILING).toString());
        } else {
            throw new IllegalArgumentException("[ Discount out of boundary. The allowed value should be between " + MIN_DISCOUNT_VALUE + " and " + MAX_DISCOUNT_VALUE + ". Value found: " + discountValue + " ]");
        }

        results.put(OFFER_TEXT_KEY, String.format("%s %s%% off: -£%s%n", product.getName(), fixedPercentageOff, totalDiscountPerProduct.setScale(2, RoundingMode.CEILING)));
        return results;
    }
}
