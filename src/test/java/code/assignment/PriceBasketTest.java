package code.assignment;

import code.assignment.exception.OfferNotFoundException;
import code.assignment.exception.ProductNotFoundException;
import code.assignment.exception.PropertiesFileNotFound;
import code.assignment.model.Product;
import code.assignment.service.CheckoutService;
import code.assignment.service.ReceiptService;
import code.assignment.service.impl.BasketServiceImpl;
import code.assignment.service.impl.CheckoutServiceImpl;
import code.assignment.service.impl.StockServiceImpl;
import code.assignment.util.FileReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

import static code.assignment.util.Constants.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PriceBasketTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldCorrectCheckoutWithoutAnyOffers() {
        // given
        String[] items = {"Milk", "Bread"};
        Map<String, Object> checkoutNoOffers = PriceBasket.process(items);

        // when
        BigDecimal subtotal = (BigDecimal) checkoutNoOffers.get(SUBTOTAL_KEY);
        BigDecimal totalDiscount = (BigDecimal) checkoutNoOffers.get(DISCOUNT_KEY);
        StringBuilder offerText = (StringBuilder) checkoutNoOffers.get(OFFER_TEXT_KEY);
        String receipt = ReceiptService.prepareToPrint(checkoutNoOffers);

        // then -> [subtotal/total: 1.30+0.80=2.10]
        assertEquals("2.10", subtotal.toString());
        assertEquals("0.00", totalDiscount.toString());
        assertThat(offerText.toString(), isEmptyOrNullString());
        assertThat(receipt, containsString("Subtotal: £2.10"));
        assertThat(receipt, containsString("(no offers available)"));
        assertThat(receipt, containsString("Total to pay: £2.10"));
    }

    @Test
    public void shouldCorrectCheckoutAcceptBuy2GetProductForHalfPrice() {
        // given
        // System.setIn(new ByteArrayInputStream("yes".getBytes()));
        String[] items = {"Soup", "Soup"};
        Map<String, Object> checkoutNoOffers = PriceBasket.process(items);

        // when
        BigDecimal subtotal = (BigDecimal) checkoutNoOffers.get(SUBTOTAL_KEY);
        BigDecimal totalDiscount = (BigDecimal) checkoutNoOffers.get(DISCOUNT_KEY);
        String receipt = ReceiptService.prepareToPrint(checkoutNoOffers);

        // then -> [subtotal: 2*0.65+0.80=2.10 , discount: 0.80/2=1.60]
        assertEquals("2.10", subtotal.toString());
        assertEquals("0.40", totalDiscount.toString());
        assertThat(receipt, containsString("Subtotal: £2.10"));
        assertThat(receipt, containsString("Bread 50% off: -£0.40"));
        assertThat(receipt, containsString("Total discount : -£0.40"));
        assertThat(receipt, containsString("Total to pay: £1.70"));
    }

    @Test
    public void shouldCorrectCheckoutMultipleBuy2GetProductForHalfPrice() {
        // given
        String[] items = {"Soup", "Soup", "Soup", "Soup"};
        Map<String, Object> checkoutNoOffers = PriceBasket.process(items);

        // when
        BigDecimal subtotal = (BigDecimal) checkoutNoOffers.get(SUBTOTAL_KEY);
        BigDecimal totalDiscount = (BigDecimal) checkoutNoOffers.get(DISCOUNT_KEY);
        String receipt = ReceiptService.prepareToPrint(checkoutNoOffers);

        // then -> [subtotal: (4*0.65)+(2*0.80)=4.20, discount: 1.60-50%=0.80]
        assertEquals("4.20", subtotal.toString());
        assertEquals("0.80", totalDiscount.toString());
        assertThat(receipt, containsString("Subtotal: £4.20"));
        assertThat(receipt, containsString("Bread 50% off: -£0.80"));
        assertThat(receipt, containsString("Total to pay: £3.40"));
    }

    @Test
    public void shouldCorrectCheckoutOneDiscountOffer() {
        // given
        String[] items = {"Apples"};
        Map<String, Object> checkoutNoOffers = PriceBasket.process(items);

        // when
        BigDecimal subtotal = (BigDecimal) checkoutNoOffers.get(SUBTOTAL_KEY);
        BigDecimal totalDiscount = (BigDecimal) checkoutNoOffers.get(DISCOUNT_KEY);
        String receipt = ReceiptService.prepareToPrint(checkoutNoOffers);

        // then -> [subtotal: 1.00 , discount: 1.00-10%=0.10, total: 1.00-0.10=0.90]
        assertEquals("1.00", subtotal.toString());
        assertEquals("0.10", totalDiscount.toString());
        assertThat(receipt, containsString("Subtotal: £1.00"));
        assertThat(receipt, containsString("Apples 10% off: -£0.10"));
        assertThat(receipt, containsString("Total discount : -£0.10"));
        assertThat(receipt, containsString("Total to pay: £0.90"));
    }

    @Test
    public void shouldCorrectCheckoutMultiplyDiscountOffer() {
        // given
        String[] items = {"Apples", "Apples", "Apples"};
        Map<String, Object> checkoutNoOffers = PriceBasket.process(items);

        // when
        BigDecimal subtotal = (BigDecimal) checkoutNoOffers.get(SUBTOTAL_KEY);
        BigDecimal totalDiscount = (BigDecimal) checkoutNoOffers.get(DISCOUNT_KEY);
        String receipt = ReceiptService.prepareToPrint(checkoutNoOffers);

        // then -> [subtotal: 3*1.00=3.00 , discount: 3.00-10%=0.30, total: 3.00-0.30=2.70]
        assertEquals("3.00", subtotal.toString());
        assertEquals("0.30", totalDiscount.toString());
        assertThat(receipt, containsString("Subtotal: £3.00"));
        assertThat(receipt, containsString("Apples 10% off: -£0.30"));
        assertThat(receipt, containsString("Total discount : -£0.30"));
        assertThat(receipt, containsString("Total to pay: £2.70"));
    }

    @Test
    public void shouldCorrectCheckoutMultiplyDifferentOffers() {
        // given
        System.setIn(new ByteArrayInputStream("yes".getBytes()));
        String[] items = {"Soup", "Apples", "Soup", "Apples"};
        Map<String, Object> checkoutNoOffers = PriceBasket.process(items);

        // when
        BigDecimal subtotal = (BigDecimal) checkoutNoOffers.get(SUBTOTAL_KEY);
        BigDecimal totalDiscount = (BigDecimal) checkoutNoOffers.get(DISCOUNT_KEY);
        String receipt = ReceiptService.prepareToPrint(checkoutNoOffers);

        // then -> [subtotal: (2*1.00)+(2*0.65)+(0.80)=4.10 , discount: (2.00-10%)-(0.80-50%)=0.60, total: 4.00-0.60=3.50]
        assertEquals("4.10", subtotal.toString());
        assertEquals("0.60", totalDiscount.toString());
        assertThat(receipt, containsString("Subtotal: £4.10"));
        assertThat(receipt, containsString("Apples 10% off: -£0.20"));
        assertThat(receipt, containsString("Bread 50% off: -£0.40"));
        assertThat(receipt, containsString("Total discount : -£0.60"));
        assertThat(receipt, containsString("Total to pay: £3.50"));
    }

    @Test
    public void shouldCorrectCheckoutMultipleProducts() {
        // given
        String[] items = {"Soup", "Bread", "Milk", "Apples"};
        Map<String, Object> checkoutNoOffers = PriceBasket.process(items);

        // when
        BigDecimal subtotal = (BigDecimal) checkoutNoOffers.get(SUBTOTAL_KEY);
        BigDecimal totalDiscount = (BigDecimal) checkoutNoOffers.get(DISCOUNT_KEY);
        String receipt = ReceiptService.prepareToPrint(checkoutNoOffers);

        // then -> [subtotal: 0.65+0.80+1.30+1.00=3.70 , discount: 1.00-10%=0.10, total: 4.00-0.10=3.60]
        assertEquals("3.75", subtotal.toString());
        assertEquals("0.10", totalDiscount.toString());
        assertThat(receipt, containsString("Subtotal: £3.75"));
        assertThat(receipt, containsString("Apples 10% off: -£0.10"));
        assertThat(receipt, containsString("Total discount : -£0.10"));
        assertThat(receipt, containsString("Total to pay: £3.65"));
    }

    @Test
    public void shouldThrowExceptionWhenInputListIsEmpty() {
        //exception
        exception.expect(IllegalArgumentException.class);

        //when
        String[] items = {};
        PriceBasket.process(items);
    }

    @Test
    public void shouldThrowExceptionWhenInputItemNotFound() {
        //exception
        exception.expect(ProductNotFoundException.class);
        exception.expectMessage("[ Product not exists in the current stock: fake-item ]");

        // when
        String[] items = {"fake-item"};
        PriceBasket.process(items);
    }

    @Test
    public void shouldThrowExceptionWhenPropertiesFileNotFound() {
        //exception
        exception.expect(PropertiesFileNotFound.class);
        exception.expectMessage("[ Unable to find stock properties file: wrong-file-name ]");

        // when
        new StockServiceImpl().getStockEndpoint("wrong-file-name", PROPERTY_NAME);
    }

    @Test
    public void shouldThrowExceptionWhenPropertyNotFound() {
        //exception
        exception.expect(InvalidParameterException.class);
        exception.expectMessage("[ Unable to get property name: wrong-property-name ]");

        // when
        new StockServiceImpl().getStockEndpoint(PROPERTIES_FILE, "wrong-property-name");
    }

    @Test
    public void shouldThrowExceptionWhenStockProductNameNull() {
        //exception
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("[ One or more product name in current stock are null. Please check the stock file. ]");

        // given
        String json = "{\"productList\":[{\"id\":1,\"name\":null,\"price\":1.20}]}";
        String[] items = {"Ham"};
        List<Product> productList = new StockServiceImpl().createStock(json).getProductList();

        // when
        new BasketServiceImpl(items, productList).createNewBasket();
    }

    @Test
    public void shouldThrowExceptionWhenOfferNotFound() {
        // code.assignment.exception
        exception.expect(OfferNotFoundException.class);
        exception.expectMessage("[ Unable to found offer: some-none-existing-promo ]");

        // given
        String json = "{\"productList\":[{\"id\":1,\"name\":\"Ham\",\"price\":1.20,\"promotion\":{\"offerName\":\"some-none-existing-promo\",\"extraProductId\":4}}]}";
        String[] items = {"Ham"};
        List<Product> productList = new StockServiceImpl().createStock(json).getProductList();

        // when
        Map<Product, Integer> basket = new BasketServiceImpl(items, productList).createNewBasket();
        CheckoutService checkoutService = new CheckoutServiceImpl(basket, productList);
        checkoutService.checkout();
    }

    @Test
    public void shouldThrowExceptionWhenDiscountOutOfRange() {
        // code.assignment.exception
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("[ Discount out of boundary. The allowed value should be between " + MIN_DISCOUNT_VALUE + " and " + MAX_DISCOUNT_VALUE + ". Value found: 1.15 ]");

        // given
        String json = "{\"productList\":[{\"id\":3,\"name\":\"Beer\",\"price\":0.99,\"promotion\":{\"offerName\":\"DISCOUNT\",\"discountValue\":1.15}}]}";
        String[] items = {"Beer"};
        List<Product> productList = new StockServiceImpl().createStock(json).getProductList();

        // when
        Map<Product, Integer> basket = new BasketServiceImpl(items, productList).createNewBasket();
        CheckoutService checkoutService = new CheckoutServiceImpl(basket, productList);
        checkoutService.checkout();
    }


    @Test
    public void shouldThrowExceptionWhenItemPriceNegative() {
        // code.assignment.exception
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("[ Product (Beer) price must be a positive value ]");

        // given
        String json = "{\"productList\":[{\"id\":3,\"name\":\"Beer\",\"price\":-0.99,\"promotion\":{\"offerName\":\"DISCOUNT\",\"discountValue\":0.15}}]}";
        String[] items = {"Beer"};
        List<Product> productList = new StockServiceImpl().createStock(json).getProductList();

        // when
        new BasketServiceImpl(items, productList).createNewBasket();
    }

    @Test
    public void shouldThrowExceptionWhenOfferExtraProductNotFounds() {
        // code.assignment.exception
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("[ Product included in the promotion cannot be null ]");

        // given
        String json = "{\"productList\":[{\"id\":1,\"name\":\"Soup\",\"price\":1.20,\"promotion\":{\"offerName\":\"BUY_2_GET_PRODUCT_FOR_HALF_PRICE\"}}]}";
        String[] items = {"Soup", "Soup"};
        List<Product> productList = new StockServiceImpl().createStock(json).getProductList();

        // when
        Map<Product, Integer> basket = new BasketServiceImpl(items, productList).createNewBasket();
        CheckoutService checkoutService = new CheckoutServiceImpl(basket, productList);
        checkoutService.checkout();
    }

    @Test
    public void shouldThrowExceptionWhenFilePathNotFound() {
        // code.assignment.exception
        exception.expect(PropertiesFileNotFound.class);
        exception.expectMessage("[ Unable to find stock properties file: wrong-file-name ]");

        // when
        FileReader.get("wrong-file-name");
    }

}