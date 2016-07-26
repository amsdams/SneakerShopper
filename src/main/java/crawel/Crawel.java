package crawel;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Crawel {
	private static final int MYTHREADS = 30;
	private static final Logger LOGGER = LoggerFactory.getLogger(Crawel.class);

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		ShopFactory shopFactory = new ShopFactory();

		Shop shop1 = shopFactory.getShop(ShopFactory.SHOPTYPES.OVERKILLSHOP);
		Shop shop2 = shopFactory.getShop(ShopFactory.SHOPTYPES.TITOLOSHOP);
		Shop shop3 = shopFactory.getShop(ShopFactory.SHOPTYPES.SNEAKAVENUE);
		Shop shop4 = shopFactory.getShop(ShopFactory.SHOPTYPES._43EINHALB);
		Shop shop5 = shopFactory.getShop(ShopFactory.SHOPTYPES._5POINTZ);
		Shop shop6 = shopFactory.getShop(ShopFactory.SHOPTYPES.ENDCLOTHING);
		
		ShopList shopList = new ShopList();
		shopList.addShop(shop1);
		shopList.addShop(shop2);
		shopList.addShop(shop3);
		shopList.addShop(shop4);
		shopList.addShop(shop5);
		shopList.addShop(shop6);
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
		writeProductList(allProducts);
		
	}
	
	private static void writeProductList(ProductList allProducts){
		
			ObjectMapper mapper = new ObjectMapper();

			

			try {
				// Convert object to JSON string and save into a file directly
				//mapper.writeValue(new File("allProducts.json"), allProducts);

				// Convert object to JSON string
				//String jsonInString = mapper.writeValueAsString(allProducts);
				//System.out.println(jsonInString);

				// Convert object to JSON string and pretty print
				//String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(allProducts);
				//System.out.println(jsonInString);
				
				
				
				File input = new File("index.html");
				Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
				doc.head().append("test");
				

			} catch (JsonGenerationException e) {
				LOGGER.error("could not generate json", e);
			} catch (JsonMappingException e) {
				LOGGER.error("could not map json", e);
			} catch (IOException e) {
				LOGGER.error("could not write file", e);
			}
		
	}

}
