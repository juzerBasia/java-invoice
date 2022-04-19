package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private Map<Product, Integer> products = new HashMap<>();
	private static int nextNumber = 0;
	private final int number = ++nextNumber;

	public void addProduct(Product product) {
		addProduct(product, 1);

	}

	public void addProduct(Product product, Integer quantity) {
		if (product == null || quantity <= 0) {
			throw new IllegalArgumentException();
		}
		int quantityBefore = 0;
		for (Product p : products.keySet()) {
			if (p.getName().equals(product.getName())) {
				quantityBefore = products.get(p);
			}
		}
		quantity = quantity + quantityBefore;
		products.put(product, quantity);
	}

	//zakladam ze akcyze wlicza sie do podstawy opodatkowania vat
	public BigDecimal getNetTotal() {
		BigDecimal totalNet = BigDecimal.ZERO;
		for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			totalNet = totalNet.add(product.getPrice().multiply(quantity));
		}
		return totalNet;
	}

	public BigDecimal getTaxTotal() {
		BigDecimal totalTax = BigDecimal.ZERO;
		for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			totalTax = totalTax.add(product.getTaxPercent().multiply(quantity).multiply(product.getPrice()));
		}
		return totalTax;
	}

	public BigDecimal getTotalExcise() {
		BigDecimal totalExcise = BigDecimal.ZERO;
		for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			totalExcise = totalExcise.add(product.getExcise().multiply(quantity));
		}
		return totalExcise;
	}

	public BigDecimal getGrossTotal() {
		BigDecimal totalGross = BigDecimal.ZERO;
		for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity)).add(product.getExcise().multiply(quantity));
		}
		return totalGross;
	}

	public int getNumber() {
		return number;
	}

	public String print() {
		String invoiceNumber = "Invoice number: " + getNumber() + "\n";

		for (Product product : products.keySet()) {
			invoiceNumber = invoiceNumber + product.getName() + ", item no: " + products.get(product)
					+ ", price/item: " + product.getPrice() + " PLN\n";
		}
		invoiceNumber += "Number of items: " + this.products.size();
		return invoiceNumber;

	}

	public Integer countItems() {
		return products.size();
	}
}
