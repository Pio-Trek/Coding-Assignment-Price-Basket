package code.assignment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static code.assignment.util.Constants.*;

/**
 * This code.assignment.service layer class is responsible for processing the receipt for print.
 */

public interface ReceiptService {

    /**
     * This method calculates total price to pay and append all messages and save
     * it into String for printing purposes.
     *
     * @param checkoutResult checkout process result with subtotal,
     *                       total discount and offers text values
     * @return receipt as String
     */
    static String prepareToPrint(Map<String, Object> checkoutResult) {
        BigDecimal subtotal = (BigDecimal) checkoutResult.get(SUBTOTAL_KEY);
        BigDecimal totalDiscount = (BigDecimal) checkoutResult.get(DISCOUNT_KEY);
        StringBuilder offerText = (StringBuilder) checkoutResult.get(OFFER_TEXT_KEY);

        totalDiscount = totalDiscount.setScale(2, RoundingMode.CEILING);

        // calculate the total price to pay to display on a receipt
        BigDecimal totalAfterDiscount = subtotal.subtract(totalDiscount);

        StringBuilder checkoutTextResult = new StringBuilder();
        checkoutTextResult
                .append(String.format("Subtotal: £%s %n", subtotal
                        .setScale(2, RoundingMode.CEILING)))
                .append("---------------------------\n")
                .append(offerText);

        if (totalDiscount.compareTo(BigDecimal.ZERO) == 0) {
            // add no offer text when no any offer was apply
            checkoutTextResult.append("(no offers available)\n");
        } else {
            checkoutTextResult
                    .append(String.format("* Total discount : -£%s *%n", totalDiscount));
        }

        checkoutTextResult
                .append("---------------------------\n")
                .append(String.format("Total to pay: £%s", totalAfterDiscount
                        .setScale(2, RoundingMode.CEILING)));

        return checkoutTextResult.toString();
    }
}
