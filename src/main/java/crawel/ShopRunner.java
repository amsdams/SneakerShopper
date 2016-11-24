package crawel;

import crawel.pojo.Shop;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShopRunner implements Runnable {

	private final Shop shop;

	ShopRunner(Shop shop) {
		this.shop = shop;
	}

	@Override
	public void run() {
		try {
			if (shop.getRunnable().equals(Boolean.FALSE)) {
				log.info("will not run shop {}", shop.getBaseUrl());
			} else {
				shop.addProductsToList(shop.getBaseUrl());

			}
		} catch (Exception e) {
			log.error("error ", e);
		}
	}
}
