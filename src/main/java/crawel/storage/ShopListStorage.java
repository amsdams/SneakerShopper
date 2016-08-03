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
	private static final Logger LOGGER = LoggerFactory.getLogger(ShopListStorage.class);

	public static ShopList get() {
		ObjectMapper mapper = new ObjectMapper();
		ShopList allShops = new ShopList();
		try {
			allShops = mapper.readValue(new File("allShops.json"), ShopList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file", e);
		}
		return allShops;

	}

	public static void print(ShopList shopList) {
		for (Shop shop : shopList.getShops()) {
			LOGGER.info(shop.toString());
		}
		LOGGER.info("printed "+ shopList.getShops().size() );
	}

	public static void put(ShopList allShops) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("allShops.json"), allShops);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
}
