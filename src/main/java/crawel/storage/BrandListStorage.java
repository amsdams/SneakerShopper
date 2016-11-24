package crawel.storage;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.Brand;
import crawel.pojo.BrandList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrandListStorage {
	private static final String ALL_BRANDS_JSON = "allBrands.json";

	private static final BrandListStorage instance = new BrandListStorage();

	public static BrandList get() {
		return get(ALL_BRANDS_JSON);

	}

	public static BrandList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		BrandList allBrands = new BrandList();
		try {
			allBrands = mapper.readValue(new File(fileName), BrandList.class);
		} catch (IOException e) {
			log.error("could not open file, creating one", e);
			BrandList brandList = new BrandList();

			Brand brand = new Brand("PUMA X ALIFE");
			Brand brandX = new Brand("ALIFE X PUMA");
			brandList.addBrand(brand);
			brandList.addBrand(brandX);
			put(brandList);
		}

		return allBrands;

	}

	public static BrandListStorage getInstance() {
		return instance;
	}

	public static void print(BrandList brandList) {
		for (Brand brand : brandList.getBrands()) {
			log.info(brand.toString());
		}
		log.info("printed " + brandList.getBrands().size());

	}

	public static void put(BrandList brandList) {

		put(brandList, ALL_BRANDS_JSON);

	}

	public static void put(BrandList brandList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), brandList);

		} catch (JsonGenerationException e) {
			log.error("could not generate json", e);
		} catch (JsonMappingException e) {
			log.error("could not map json", e);
		} catch (IOException e) {
			log.error("could not write file", e);
		}

	}

	private BrandListStorage() {
	}
}
