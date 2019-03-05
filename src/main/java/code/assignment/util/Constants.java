package code.assignment.util;

import java.math.BigDecimal;

/**
 * All used constants values in the system
 */

public class Constants {

    private Constants() {
    }

    // constants for loading properties
    public static final String PROPERTIES_FILE = "stock.properties";
    public static final String PROPERTY_NAME = "stockSourceFile";

    // constants for checkout and offer result HashMap
    public static final String SUBTOTAL_KEY = "subtotal";
    public static final String DISCOUNT_KEY = "discount";
    public static final String OFFER_TEXT_KEY = "message";

    // constants for minimum and maximum discount range
    public static final BigDecimal MIN_DISCOUNT_VALUE = new BigDecimal("0.01"); // 1% div 100 = 1
    public static final BigDecimal MAX_DISCOUNT_VALUE = new BigDecimal("0.99"); // 99% div 100 = 1

}
