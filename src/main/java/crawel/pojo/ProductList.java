package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ProductList {

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

}
