package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class ExciseProduct extends Product {
	public ExciseProduct(String name, BigDecimal price) {
		super(name, price, new BigDecimal("0"), new BigDecimal(5.56));
	}
}
