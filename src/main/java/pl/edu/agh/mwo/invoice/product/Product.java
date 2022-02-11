package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;
//exceptions checked and unchecked,
    protected Product(String name, BigDecimal price, BigDecimal tax) {
        if(name == null || name.isEmpty() ){//jezeli damy najpierw isEmpty a potem null to bedzie blad
            //bo na null bedzie chcial robic .isEmpty i null nie jest lapany
            throw new IllegalArgumentException("Product nam cannot be null or empty");
        }
//lub name == " " -i nie ma tutaj ostrzezenia choc powinno byc, bo to porownuje nam referencje, w tym wypadku
//jednak test jest zielony, bo kompilator swobie stworzyl global empty string = "", a my porownujemy te same obiekty
// -> INTERNOWANIE STRINGOW

//        if (price == null || price.intValue() < 0) {
//            throw new IllegalArgumentException("Price nam cannot be null or less than zero");}
//lub tez
        if (price == null || price.signum() == -1) {
            throw new IllegalArgumentException("Price nam cannot be null or less than zero");}


        this.name = name;
        this.price = price;
        this.taxPercent = tax;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getTaxPercent() {
        return this.taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return this.price.multiply(this.taxPercent).add(this.price);
    }
}
