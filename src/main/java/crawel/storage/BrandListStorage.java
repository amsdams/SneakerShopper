package crawel.storage;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.Brand;
import crawel.pojo.BrandList;

public class BrandListStorage {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandListStorage.class);

	public static BrandList get() {
		BrandList allBrands = get("allBrands.json");

		return allBrands;

	}

	public static BrandList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		BrandList allBrands = new BrandList();
		try {
			allBrands = mapper.readValue(new File(fileName), BrandList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file, creating one", e);
			BrandList brandList = new BrandList();

			Brand brand = new Brand("PUMA X ALIFE");
			Brand brandX = new Brand("ALIFE X PUMA");
			brandList.addBrand(brand);
			brandList.addBrand(brandX);
			put(brandList);
		}

		return allBrands;

	}

	public static void print(BrandList brandList) {
		for (Brand brand : brandList.getBrands()) {
			LOGGER.info(brand.toString());
		}
		LOGGER.info("printed " + brandList.getBrands().size());

	}

	public static void put(BrandList brandList) {

		put(brandList, "allBrands.json");

	}

	public static void put(BrandList brandList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), brandList);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
}
