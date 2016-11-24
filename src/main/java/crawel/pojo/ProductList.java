package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductList {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductList.class);

	private List<Product> products;

	public ProductList() {
		this.products = new ArrayList<Product>();
	}

	public ProductList(List<Product> collect) {
		this.products = collect;
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Product s: this.products){
			sb.append(s.toString());
		}
		return "ProductList [products=" + sb.toString() + "]";
	}
}
