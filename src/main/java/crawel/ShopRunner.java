package crawel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.pojo.Shop;

public class ShopRunner implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShopRunner.class);

	private final Shop shop;

	ShopRunner(Shop shop) {
		this.shop = shop;
	}

	@Override
	public void run() {
		try {
			if (shop.getRunnable().equals(Boolean.FALSE)) {
				LOGGER.info("will not run shop {}", shop.getBaseUrl());
			} else {
				shop.addProductsToList(shop.getBaseUrl());

			}
		} catch (Exception e) {
			LOGGER.error("error ", e);
		}
	}
}
