package crawel.storage;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.Shop;
import crawel.pojo.ShopList;

public class ShopListStorage {
	private static final String ALL_SHOPS_JSON = "allShops.json";

	private static final Logger LOGGER = LoggerFactory.getLogger(ShopListStorage.class);

	private static final ShopListStorage instance = new ShopListStorage();

	private ShopListStorage() {
	}

	public static ShopListStorage getInstance() {
		return instance;
	}

	public static ShopList get() {
		return get(ALL_SHOPS_JSON);
	}

	public static ShopList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		ShopList allShops = new ShopList();
		try {
			allShops = mapper.readValue(new File(fileName), ShopList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file, creating new one", e);

			allShops = new ShopList();

			Shop shop = new Shop();
			shop.setJavaScriptEnabled(true);
			// etc
			allShops.addShop(shop);
		}
		return allShops;

	}

	public static void print(ShopList shopList) {
		for (Shop shop : shopList.getShops()) {
			LOGGER.info(shop.toString());
		}
		LOGGER.info("printed " + shopList.getShops().size());
	}

	public static void put(ShopList allShops) {

		put(allShops, ALL_SHOPS_JSON);

	}

	public static void put(ShopList allShops, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), allShops);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
}
