package crawel;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.ProductList;

public abstract class Storage {
	private static final Logger LOGGER = LoggerFactory.getLogger(Storage.class);

	String fileName = "all.json";

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private ProductList get() {
		ObjectMapper mapper = new ObjectMapper();
		ProductList allProducts = new ProductList();
		try {
			allProducts = mapper.readValue(new File(this.getFileName()), ProductList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file", e);

		}
		return allProducts;

	}

	public void put(ProductList allProducts) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(this.getFileName()), allProducts);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
}
