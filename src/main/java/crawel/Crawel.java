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
import crawel.pojo.FileTransferList;
import crawel.pojo.Product;
import crawel.pojo.ProductList;
import crawel.pojo.Shop;
import crawel.pojo.ShopList;
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;
import crawel.storage.FileTransferListStorage;
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
			LOGGER.warn("could not read commandline {}", e.getMessage(), e);
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

			Runnable worker = new ShopRunner(shop);
			executor.execute(worker);
		}

		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}
		LOGGER.info("\nFinished all threads");

		ProductList allProducts = new ProductList();
		for (Shop shop : shopList.getShops()) {
			allProducts.getProducts().addAll(shop.getProductList().getProducts());

		}
		return allProducts;
	}

	private void goInteractive(ProductList productList) {
		boolean keepRunning = true;
		ConsoleHelper consoleHelper = new ConsoleHelper();

		ActionList actionList = consoleHelper.getActionList();
		ProductList filteredProductList = new ProductList();
		Comparator<Product> comparator = Product.NewPriceComparator;
		Comparator<Product> secondComparator = Product.NameComparator;

		while (keepRunning) {

			String in = consoleHelper.readLine(consoleHelper.printActionList(actionList));
			LOGGER.info("received " + in);
			if (in.startsWith("sbb")) {
				String order = in.replace("sbb", "");
				LOGGER.info("will sort by name and order" + order);

				comparator = Product.BrandNameComparator;

				filteredProductList.setProducts(productList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbn")) {
				String order = in.replace("sbn", "");
				LOGGER.info("will sort by name and order" + order);

				comparator = Product.NameComparator;

				filteredProductList.setProducts(productList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbp")) {
				String order = in.replace("sbp", "");
				LOGGER.info("will sort by new price" + order);

				comparator = Product.NewPriceComparator;

				filteredProductList.setProducts(productList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbs")) {
				String order = in.replace("sbs", "");
				LOGGER.info("will sort by shop and order" + order);

				comparator = Product.ShopNameComparator;

				filteredProductList.setProducts(productList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbd")) {
				String order = in.replace("sbd", "");
				LOGGER.info("will sort by discount and order" + order);

				comparator = Product.DiscountComparator;

				filteredProductList.setProducts(productList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("fbb")) {
				String brand = in.replace("fbb", "");
				LOGGER.info("extracted brand " + brand);

				filteredProductList
						.setProducts(productList.getProducts().stream().filter(p -> p.getBrandName().contains(brand))
								.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("fbn")) {
				String name = in.replace("fbn", "");
				LOGGER.info("extracted name " + name);

				filteredProductList
						.setProducts(productList.getProducts().stream().filter(p -> p.getName().contains(name))
								.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("reset")) {
				filteredProductList = productList;
				ProductListStorage.print(filteredProductList);

			} else if (in.startsWith("gp")) {
				ShopList shopList = ShopListStorage.get();
				productList = this.getShopsProductList(shopList);
				ProductListStorage.print(productList);

			} else if (in.startsWith("sp")) {

				ProductListStorage.put(productList);
			} else if (in.startsWith("ftp")) {

				FileTransferList fileTransferList = FileTransferListStorage.get();
				FileTransferListStorage.putTransfers(fileTransferList);

			} else if (in.startsWith("psl")) {
				ShopListStorage.print(ShopListStorage.get());

			} else if (in.startsWith("ppl")) {
				ProductListStorage.print(ProductListStorage.get());

			} else if (in.startsWith("pbl")) {
				BrandListStorage.print(BrandListStorage.get());
			} else if (in.startsWith("pftl")) {
				FileTransferListStorage.print(FileTransferListStorage.get());
			} else if (in.startsWith("pcl")) {
				CurrencyListStorage.print(CurrencyListStorage.get());
			} else if (in.startsWith("wsl")) {
				ShopList shopList = ShopListStorage.get();
				shopList = new ShopList(shopList.getShops().stream().sorted().collect(Collectors.toList()));

				ShopListStorage.put(shopList);

			} else if (in.startsWith("wpl")) {
				productList = new ProductList(productList.getProducts().stream().sorted().collect(Collectors.toList()));
				ProductListStorage.put(productList);

			} else if (in.startsWith("wbl")) {
				BrandList brandList = BrandListStorage.get();
				brandList = new BrandList(brandList.getBrands().stream().sorted().collect(Collectors.toList()));
				BrandListStorage.put(brandList);

			} else if (in.startsWith("wcl")) {
				CurrencyList currencyList = CurrencyListStorage.get();
				currencyList = new CurrencyList(
						currencyList.getCurrencys().stream().sorted().collect(Collectors.toList()));
				CurrencyListStorage.put(currencyList);
			} else if (in.startsWith("bye")) {
				keepRunning = false;

			}

		}

	}

}
