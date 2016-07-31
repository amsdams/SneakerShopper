package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductList {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductList.class);


	private List<Product> Products = new ArrayList<Product>();

	public void addProduct(Product Product) {
		Products.add(Product);
	}

	public List<Product> getProducts() {
		return Products;
	}

	public void setProducts(List<Product> Products) {
		this.Products = Products;
	}
}
