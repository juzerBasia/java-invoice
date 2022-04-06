package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;
    private Product cukier = new TaxFreeProduct("Cukier", new BigDecimal("5"));
    private Product mleczko = new DairyProduct("Mleko", new BigDecimal("10"));
    private Product owoce = new TaxFreeProduct("Owoce", new BigDecimal("10"));
    private Product bottleOfWine = new ExciseProduct("lambrusko",new BigDecimal(12));
    private Product fuel = new ExciseProduct("FuelCanister",new BigDecimal(12));

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));

    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testInvoiceHasNumberGreaterThan0() {
        int number = invoice.getNumber();
        Assert.assertThat(number, Matchers.greaterThan(0));
    }

    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertNotEquals(number1, number2);
    }

    @Test
    public void testInvoiceDoesNotChangeItsNumber() {
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }

    @Test
    public void testTheFirstInvoiceNumberIsLowerThanTheSecond() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertThat(number1, Matchers.lessThan(number2));
    }
    @Test
    public void testTheSecondInvoiceNumberIsOneMoreThanTheSecond() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertThat(number2, Matchers.comparesEqualTo(number1+1));
    }

    @Test
    public void testPrintInvoice(){
        Invoice invoice = new Invoice();

        String expectedResults = "Invoice number: " + invoice.getNumber() + "\n" + "Owoce, item no: 5, price/item: 10 PLN" + "\n" +"Cukier, item no: 10, price/item: 5 PLN" + "\n" +  "Number of items: 2";
        invoice.addProduct(cukier, 10);
        invoice.addProduct(owoce, 5);
        Assert.assertEquals(expectedResults, invoice.print());
    }

    @Test
    public void testCheckInvoiceIfDuplicates(){
        Invoice invoice2 = new Invoice();
        String expectedResults = "Invoice number: " + invoice2.getNumber() + "\n" + "Owoce, item no: 1, price/item: 10 PLN" + "\n" +"Cukier, item no: 30, price/item: 5 PLN" + "\n" +  "Number of items: 2";
//        Product cukier = new TaxFreeProduct("Cukier", new BigDecimal("5"));
        invoice2.addProduct(mleczko, 1);
        invoice2.addProduct(cukier, 10);
        invoice2.addProduct(cukier, 20);
        Assert.assertThat(new Integer("2"), Matchers.comparesEqualTo(invoice2.countItems()));
    }

    //test for newly added excise products
    @Test
    public void testInvoiceHasProperExcise() {
          //excise 5.56 per bottle = 55.60 in total
        invoice.addProduct(bottleOfWine, 10);
        Assert.assertThat(new BigDecimal("55.60"), Matchers.equalTo(invoice.getTotalExcise()));
    }

    @Test
    public void testInvoiceHasProperGrossOnExciseProducts() {
        //excise 5.56 per bottle = 55.60 in total
        //price per bottle 12 total price 10*12+55.60 = 175.60, same gross for fulwe
        invoice.addProduct(bottleOfWine, 10);
        invoice.addProduct(fuel, 10);
        Assert.assertThat(new BigDecimal("351.20"), Matchers.equalTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForExciseProductsPlusMleczko() {
        invoice.addProduct(bottleOfWine, 10);
        invoice.addProduct(fuel, 10);
        invoice.addProduct(mleczko, 1);
        Assert.assertThat(new BigDecimal("250"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }
    @Test
    public void testInvoiceHasProperSubtotalForExciseProductPlusMleczko() {
        invoice.addProduct(bottleOfWine, 10);
        invoice.addProduct(fuel, 10);
        invoice.addProduct(mleczko, 1);
        Assert.assertThat(new BigDecimal("0.8"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

}
