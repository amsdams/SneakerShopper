package crawel.storage;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.Shop;
import crawel.pojo.ShopList;
import lombok.extern.slf4j.Slf4j;

@Slf4j

public class ShopListStorage {
	private static final String ALL_SHOPS_JSON = "allShops.json";

	private static final ShopListStorage instance = new ShopListStorage();

	public static ShopList get() {
		return get(ALL_SHOPS_JSON);
	}

	public static ShopList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		ShopList allShops = new ShopList();
		try {
			allShops = mapper.readValue(new File(fileName), ShopList.class);
		} catch (IOException e) {
			log.error("could not open file, creating new one", e);

			allShops = new ShopList();

			Shop shop = new Shop();
			shop.setJavaScriptEnabled(true);
			// etc
			allShops.addShop(shop);
		}
		return allShops;

	}

	public static ShopListStorage getInstance() {
		return instance;
	}

	public static void print(ShopList shopList) {
		for (Shop shop : shopList.getShops()) {
			log.info(shop.toString());
		}
		log.info("printed " + shopList.getShops().size());
	}

	public static void put(ShopList allShops) {

		put(allShops, ALL_SHOPS_JSON);

	}

	public static void put(ShopList allShops, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), allShops);

		} catch (JsonGenerationException e) {
			log.error("could not generate json", e);
		} catch (JsonMappingException e) {
			log.error("could not map json", e);
		} catch (IOException e) {
			log.error("could not write file", e);
		}

	}

	private ShopListStorage() {
	}
}
