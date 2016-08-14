package crawel.storage;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.helpers.PriceHelper;
import crawel.pojo.FileTransferList;
import crawel.pojo.Product;
import crawel.pojo.ProductList;

public class ProductListStorage {
	private static final String ALL_PRODUCTS_JSON = "allProducts.json";
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductListStorage.class);
	private static final ProductListStorage instance = new ProductListStorage();

	private ProductListStorage() {
	}

	public static ProductListStorage getInstance() {
		return instance;
	}

	public static ProductList get() {
		return get(ALL_PRODUCTS_JSON);
	}

	public static ProductList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		ProductList allProducts = new ProductList();
		try {
			allProducts = mapper.readValue(new File(fileName), ProductList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file", e);

		}
		return allProducts;

	}

	public static void print(ProductList productList) {
		for (Product product : productList.getProducts()) {
			LOGGER.info(product.toString());
		}
		LOGGER.info("printed " + productList.getProducts().size());
	}

	public static void put(ProductList productList) {

		put(productList, ALL_PRODUCTS_JSON);

	}
	
	public static void put(ProductList productList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), productList);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
}
