package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductList {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductList.class);

	private List<Product> products = new ArrayList<Product>();

	public ProductList(List<Product> collect) {
		this.products = collect;
	}

	public ProductList() {
	}

	public void addProduct(Product Product) {
		products.add(Product);
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> Products) {
		this.products = Products;
	}
}
