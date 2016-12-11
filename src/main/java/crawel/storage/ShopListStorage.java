package crawel.storage;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.Constants;
import crawel.pojo.Shop;
import crawel.pojo.ShopList;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
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
		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION_CREATING_NEW, e.getMessage(), e);
			}
			allShops = new ShopList();
			allShops.setLimitEnabled(false);
			allShops.setDetailsEnabled(false);
			allShops.setJavaScriptEnabled(false);
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

		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}

	}

	
}
