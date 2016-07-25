package crawel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Crawel {
	private static final int MYTHREADS = 30;
	private static final Logger LOGGER = LoggerFactory.getLogger(Crawel.class);

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		ShopFactory shopFactory = new ShopFactory();

		Shop shop1 = shopFactory.getShop(ShopFactory.SHOPTYPES.OVERKILLSHOP);
		Shop shop2 = shopFactory.getShop(ShopFactory.SHOPTYPES.TITOLOSHOP);
		Shop shop3 = shopFactory.getShop(ShopFactory.SHOPTYPES.SNEAKAVENUE);

		ShopList shopList = new ShopList();
		shopList.addShop(shop1);
		shopList.addShop(shop2);
		shopList.addShop(shop3);

		for (Shop shop : shopList.getShops()) {

			Runnable worker = new Shopper(shop);
			executor.execute(worker);
		}

		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {
			// System.out.println("\nnot terminated yet");
		}
		LOGGER.info("\nFinished all threads");

		ProductList allProducts = new ProductList();
		for (Shop shop : shopList.getShops()) {
			allProducts.getProducts().addAll((shop.getProductList().getProducts()));

		}

		for (Product product : allProducts.getProducts()) {
			LOGGER.info(product.toString());
		}
	}

}
