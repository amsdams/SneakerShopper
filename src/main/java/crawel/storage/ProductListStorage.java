package crawel.storage;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.Product;
import crawel.pojo.ProductList;

public class ProductListStorage {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductListStorage.class);

	public static ProductList get() {
		ObjectMapper mapper = new ObjectMapper();
		ProductList allProducts = new ProductList();
		try {
			allProducts = mapper.readValue(new File("allProducts.json"), ProductList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file", e);

		}
		return allProducts;

	}
	public static void print(ProductList productList) {
		for (Product product : productList.getProducts()) {
			LOGGER.info(product.toString());
		}
		LOGGER.info("printed "+ productList.getProducts().size() );
	}

	public static void   put(ProductList productList ) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("allProducts.json"), productList);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
}
