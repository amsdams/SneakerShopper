package crawel;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.console.ActionList;
import crawel.console.ConsoleHelper;

public class Crawel {
	private static final int MYTHREADS = 30;
	private static final Logger LOGGER = LoggerFactory.getLogger(Crawel.class);

	public static void main(String[] args) {
		Crawel crawel = new Crawel();
		CmdLineParser parser = new CmdLineParser(crawel);
		ConsoleHelper consoleHelper = new ConsoleHelper();

		try {
			parser.parseArgument(args);
			if (crawel.writeFile) {

				ShopList shopList = crawel.getShopList();
				ProductList productList = crawel.getShopsProductList(shopList);
				consoleHelper.printProductList(productList);
				writeProductList(productList);
			}

			if (crawel.openFile) {
				ProductList productList = openProductList();
				consoleHelper.printProductList(productList);

			}
			if (crawel.isInterative) {
				ProductList productList = openProductList();
				crawel.goInteractive(productList);
			}

		} catch (CmdLineException e) {
			// handling of wrong arguments
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
		}

	}

	private static ProductList openProductList() {
		ObjectMapper mapper = new ObjectMapper();
		ProductList allProducts = new ProductList();
		try {
			allProducts = mapper.readValue(new File("allProducts.json"), ProductList.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allProducts;

	}

	private static void writeProductList(ProductList allProducts) {

		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert object to JSON string and save into a file directly
			mapper.writeValue(new File("allProducts.json"), allProducts);

			// Convert object to JSON string
			// String jsonInString = mapper.writeValueAsString(allProducts);
			// System.out.println(jsonInString);

			// Convert object to JSON string and pretty print
			// String jsonInString =
			// mapper.writerWithDefaultPrettyPrinter().writeValueAsString(allProducts);
			// System.out.println(jsonInString);

			/*
			 * File input = new File("index.html"); Document doc =
			 * Jsoup.parse(input, "UTF-8", "http://example.com/");
			 * doc.head().append("test");
			 */

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}

	@Option(name = "--interactive", usage = "start interactive")
	private Boolean isInterative = false;

	@Option(name = "--write", usage = "write file")
	private Boolean writeFile = false;

	@Option(name = "--open", usage = "open file")
	private Boolean openFile = false;

	private ShopList getShopList() {
		ShopFactory shopFactory = new ShopFactory();
		Shop shop1 = shopFactory.getShop(ShopFactory.SHOPTYPES.OVERKILLSHOP);
		Shop shop2 = shopFactory.getShop(ShopFactory.SHOPTYPES.TITOLOSHOP);
		Shop shop3 = shopFactory.getShop(ShopFactory.SHOPTYPES.SNEAKAVENUE);
		Shop shop4 = shopFactory.getShop(ShopFactory.SHOPTYPES._43EINHALB);
		Shop shop5 = shopFactory.getShop(ShopFactory.SHOPTYPES._5POINTZ);
		Shop shop6 = shopFactory.getShop(ShopFactory.SHOPTYPES.ENDCLOTHING);
		Shop shop7 = shopFactory.getShop(ShopFactory.SHOPTYPES.SNEAKERBAAS);
		ShopList shopList = new ShopList();
		shopList.addShop(shop1);
		shopList.addShop(shop2);
		shopList.addShop(shop3);
		shopList.addShop(shop4);
		shopList.addShop(shop5);
		shopList.addShop(shop6);
		shopList.addShop(shop7);
		return shopList;
	}

	private ProductList getShopsProductList(ShopList shopList) {
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);

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
		return allProducts;
	}

	private void goInteractive(ProductList productList) {
		boolean keepRunning = true;
		ConsoleHelper consoleHelper = new ConsoleHelper();

		ActionList actionList = consoleHelper.getActionList();
		ProductList filteredProductList = new ProductList();
		while (keepRunning) {

			String in = consoleHelper.readLine(consoleHelper.printActionList(actionList));
			LOGGER.info("received " + in);
			if (in.startsWith("fbb")) {
				String brand = in.replace("fbb", "");
				LOGGER.info("extracted brand " + brand);

				Comparator<Product> comparator = Product.NewPriceComparator;

				filteredProductList.setProducts(productList.getProducts().stream()
						.filter(p -> p.getBrandName().contains(brand)).sorted(comparator).collect(Collectors.toList()));
				consoleHelper.printProductList(filteredProductList);
			} else if (in.startsWith("fbn")) {
				String name = in.replace("fbn", "");
				LOGGER.info("extracted name " + name);
				Comparator<Product> comparator = Product.NewPriceComparator;

				filteredProductList.setProducts(productList.getProducts().stream()
						.filter(p -> p.getName().contains(name)).sorted(comparator).collect(Collectors.toList()));
				consoleHelper.printProductList(filteredProductList);
			} else if (in.startsWith("reset")) {
				filteredProductList = productList;
				consoleHelper.printProductList(filteredProductList);
			} else if (in.startsWith("bye")) {
				keepRunning = false;

			}
			/*
			 * for (Action action: actionList.getActions()){ if
			 * (in.equals(action.getAction())){ action.doAction(); } }
			 */

		}

	}

}
