package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductList {

	private List<Product> products;

	public ProductList() {
		this.products = new ArrayList<>();
	}

	

	public void addProduct(Product product) {
		products.add(product);
	}

}
