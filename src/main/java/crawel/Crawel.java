package crawel;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.console.ConsoleHelper;
import crawel.pojo.ActionList;
import crawel.pojo.BrandList;
import crawel.pojo.CurrencyList;
import crawel.pojo.Product;
import crawel.pojo.ProductList;
import crawel.pojo.Shop;
import crawel.pojo.ShopList;
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;
import crawel.storage.ProductListStorage;
import crawel.storage.ShopListStorage;

public class Crawel {
	private static final int MYTHREADS = 30;
	private static final Logger LOGGER = LoggerFactory.getLogger(Crawel.class);

	public static void main(String[] args) {
		Crawel crawel = new Crawel();
		CmdLineParser parser = new CmdLineParser(crawel);

		try {
			parser.parseArgument(args);
			if (crawel.writeFile) {

				BrandList brandList = BrandListStorage.get();
				BrandListStorage.print(brandList);
				

				ShopList shopList = ShopListStorage.get();
				ShopListStorage.print(shopList);

				CurrencyList currencyList = CurrencyListStorage.get();
				CurrencyListStorage.print(currencyList);
				
				ProductList productList = crawel.getShopsProductList(shopList);
				ProductListStorage.print(productList);
				ProductListStorage.put(productList);
			}

			if (crawel.openFile) {
				
				ProductList productList = ProductListStorage.get();
				
				ProductListStorage.print(productList);

			}
			if (crawel.isInterative) {
				ProductList productList = ProductListStorage.get();
				crawel.goInteractive(productList);
			}

		} catch (CmdLineException e) {
			// handling of wrong arguments
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
		}

	}

	@Option(name = "--interactive", usage = "start interactive")
	private Boolean isInterative = false;

	@Option(name = "--write", usage = "write file")
	private Boolean writeFile = false;

	@Option(name = "--open", usage = "open file")
	private Boolean openFile = false;

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
		Comparator<Product> comparator = Product.NewPriceComparator;
		while (keepRunning) {

			String in = consoleHelper.readLine(consoleHelper.printActionList(actionList));
			LOGGER.info("received " + in);
			if (in.startsWith("sbb")) {
				String order = in.replace("sbb", "");
				LOGGER.info("will sort by name and order" + order);

				comparator = Product.BrandNameComparator;

				filteredProductList.setProducts(
						productList.getProducts().stream().sorted(comparator).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbn")) {
				String order = in.replace("sbn", "");
				LOGGER.info("will sort by name and order" + order);

				comparator = Product.NameComparator;

				filteredProductList.setProducts(
						productList.getProducts().stream().sorted(comparator).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbp")) {
				String order = in.replace("sbp", "");
				LOGGER.info("will sort by new price" + order);

				comparator = Product.NewPriceComparator;

				filteredProductList.setProducts(
						productList.getProducts().stream().sorted(comparator).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbs")) {
				String order = in.replace("sbs", "");
				LOGGER.info("will sort by shop and order" + order);

				comparator = Product.ShopNameComparator;

				filteredProductList.setProducts(
						productList.getProducts().stream().sorted(comparator).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("fbb")) {
				String brand = in.replace("fbb", "");
				LOGGER.info("extracted brand " + brand);

				filteredProductList.setProducts(productList.getProducts().stream()
						.filter(p -> p.getBrandName().contains(brand)).sorted(comparator).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("fbn")) {
				String name = in.replace("fbn", "");
				LOGGER.info("extracted name " + name);

				filteredProductList.setProducts(productList.getProducts().stream()
						.filter(p -> p.getName().contains(name)).sorted(comparator).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("reset")) {
				filteredProductList = productList;
				ProductListStorage.print(filteredProductList);
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
