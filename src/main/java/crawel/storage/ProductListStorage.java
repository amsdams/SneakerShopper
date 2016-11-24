package crawel.storage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.Product;
import crawel.pojo.ProductList;
import crawel.pojo.Size;
import crawel.pojo.SizeList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductListStorage {
	private static final String ALL_PRODUCTS_JSON = "allProducts.json";
	private static final ProductListStorage instance = new ProductListStorage();

	public static ProductList get() {

		return get(ALL_PRODUCTS_JSON);
	}

	public static ProductList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		ProductList allProducts = new ProductList();
		try {
			allProducts = mapper.readValue(new File(fileName), ProductList.class);
		} catch (IOException e) {
			log.error("could not open file", e);
			log.error("could not open file, creating one", e);
			allProducts = new ProductList();
			Product product = new Product();
			product.setName("yourname");
			product.setBrandName("yourbrandname");
			product.setDiscountInEU(BigDecimal.valueOf(1.0));
			product.setNewPriceRaw(BigDecimal.valueOf(1.0));
			product.setOldPriceRaw(BigDecimal.valueOf(1.0));
			product.setNewPriceInEuro(BigDecimal.valueOf(1.0));
			product.setOldPriceInEuro(BigDecimal.valueOf(1.0));
			SizeList sizesRaw = new SizeList();
			Size size = new Size();
			size.setSize("43");
			size.setMetric("eu");

			sizesRaw.addSize(size);
			product.setSizesRaw(sizesRaw);
			allProducts.addProduct(product);
			put(allProducts);

		}
		return allProducts;

	}

	public static ProductListStorage getInstance() {
		return instance;
	}

	public static void print(ProductList productList) {
		for (Product product : productList.getProducts()) {
			log.info(product.toString());
		}
		log.info("printed " + productList.getProducts().size());
	}

	public static void put(ProductList productList) {

		put(productList, ALL_PRODUCTS_JSON);

	}

	public static void put(ProductList productList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), productList);

		} catch (JsonGenerationException e) {
			log.error("could not generate json", e);
		} catch (JsonMappingException e) {
			log.error("could not map json", e);
		} catch (IOException e) {
			log.error("could not write file", e);
		}

	}

	private ProductListStorage() {
	}
}
