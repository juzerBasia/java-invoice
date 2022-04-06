package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
//    private Collection<Product> products = new ArrayList<>();//przechowuje kolekcje produktow, a jako implementacje wykorzystuje arrayList
    private Map<Product, Integer> products = new HashMap<>();
    private Map<Product, Integer> taxes = new HashMap<>();
   public void addProduct(Product product) {
//        this.products.put(product,1);
        this.addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {//ta metoda jest przeciazona
         if(quantity < 1){throw new IllegalArgumentException("quantity can not be less than 1");}
        this.products.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal sum = BigDecimal.ZERO;

        for (Product product : this.products.keySet()) {
            Integer quantity = this.products.get(product);
            BigDecimal quantityAsBigDecimal = BigDecimal.valueOf(quantity);
            sum = sum.add(product.getPrice().multiply(quantityAsBigDecimal));
        }
        return  sum;
    }

    public BigDecimal getTax() {
        BigDecimal taxAmount = BigDecimal.ZERO;
        BigDecimal sum = BigDecimal.ZERO;

        for (Product product : this.products.keySet()) {
            Integer quantity = this.products.get(product);
            BigDecimal quantityAsBigDecimal = BigDecimal.valueOf(quantity);

            taxAmount = taxAmount.add(product.getPrice().multiply(product.getTaxPercent()).multiply(quantityAsBigDecimal));
        }
        return taxAmount;
    }

    public BigDecimal getTotal() {
        return  this.getTax().add(this.getNetTotal());
    }
}
