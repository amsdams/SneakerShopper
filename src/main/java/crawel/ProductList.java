package crawel;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
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
