package crawel;

import crawel.pojo.Shop;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ShopRunner implements Runnable {

	private final Shop shop;

	@Override
	public void run() {
		try {
			if (shop.getRunnable().equals(Boolean.FALSE)) {
				log.info("will not run shop {}", shop.getBaseUrl());
			} else {
				shop.addProductsToList(shop.getBaseUrl());

			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
	}
}
