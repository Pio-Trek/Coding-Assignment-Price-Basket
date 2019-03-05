package code.assignment.model;

import java.math.BigDecimal;

/**
 * Model class used to create Promotion object.
 */

public class Promotion {

    private final String offerName;
    private final Long extraProductId;
    private final BigDecimal discountValue;

    public Promotion(String offerName, Long extraProductId, BigDecimal discountValue) {
        this.offerName = offerName;
        this.extraProductId = extraProductId;
        this.discountValue = discountValue;
    }

    public String getOfferName() {
        return offerName;
    }

    public Long getExtraProductId() {
        return extraProductId;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }
}
