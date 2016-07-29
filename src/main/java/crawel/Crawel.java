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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.console.ConsoleHelper;
import crawel.pojo.ActionList;
import crawel.pojo.Brand;
import crawel.pojo.BrandList;
import crawel.pojo.Product;
import crawel.pojo.ProductList;
import crawel.pojo.Shop;
import crawel.pojo.ShopList;

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

				BrandList brandList = crawel.getBrandList();
				consoleHelper.printBrandList(brandList);
				
				ShopList shopList = crawel.getShopList();
				consoleHelper.printShopList(shopList);
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
			LOGGER.error("could not open file", e);

		}
		return allProducts;

	}

	private static BrandList openBrandList() {
		ObjectMapper mapper = new ObjectMapper();
		BrandList allBrands = new BrandList();
		try {
			allBrands = mapper.readValue(new File("allBrands.json"), BrandList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file", e);

		}
		return allBrands;

	}
	
	private static void writeBrandListList(BrandList allBrands) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("allBrands.json"), allBrands);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
	
	private static void writeProductList(ProductList allProducts) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("allProducts.json"), allProducts);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}

	private static ShopList openShopList() {
		ObjectMapper mapper = new ObjectMapper();
		ShopList allShops = new ShopList();
		try {
			mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

			allShops = mapper.readValue(new File("allShops.json"), ShopList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file", e);
		}
		return allShops;

	}

	private static void writeShopList(ShopList allShops) {

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

	@Option(name = "--interactive", usage = "start interactive")
	private Boolean isInterative = false;

	@Option(name = "--write", usage = "write file")
	private Boolean writeFile = false;

	@Option(name = "--open", usage = "open file")
	private Boolean openFile = false;

	private ShopList getShopList() {
		ShopList shopList = openShopList();
		//writeShopList(shopList);
		
		
		return shopList;
	}
	
	private BrandList getBrandList() {
		
		BrandList brandList = new BrandList();
		
		Brand brand1  = new Brand("Adidas");
		brandList.addBrand(brand1);
		
		Brand brand2  = new Brand("Puma");
		brandList.addBrand(brand2);
		
		Brand brand3  = new Brand("Nike");
		brandList.addBrand(brand3);
		
		
		writeBrandListList(brandList);
		
		//BrandList brandList = openBrandList();
		//writeShopList(shopList);
		
		
		return brandList;
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
				consoleHelper.printProductList(filteredProductList);
			} else if (in.startsWith("sbn")) {
				String order = in.replace("sbn", "");
				LOGGER.info("will sort by name and order" + order);

				comparator = Product.NameComparator;

				filteredProductList.setProducts(
						productList.getProducts().stream().sorted(comparator).collect(Collectors.toList()));
				consoleHelper.printProductList(filteredProductList);
			} else if (in.startsWith("sbp")) {
				String order = in.replace("sbp", "");
				LOGGER.info("will sort by new price" + order);

				comparator = Product.NewPriceComparator;

				filteredProductList.setProducts(
						productList.getProducts().stream().sorted(comparator).collect(Collectors.toList()));
				consoleHelper.printProductList(filteredProductList);
			} else if (in.startsWith("sbs")) {
				String order = in.replace("sbs", "");
				LOGGER.info("will sort by shop and order" + order);

				comparator = Product.ShopNameComparator;

				filteredProductList.setProducts(
						productList.getProducts().stream().sorted(comparator).collect(Collectors.toList()));
				consoleHelper.printProductList(filteredProductList);
			} else if (in.startsWith("fbb")) {
				String brand = in.replace("fbb", "");
				LOGGER.info("extracted brand " + brand);

				filteredProductList.setProducts(productList.getProducts().stream()
						.filter(p -> p.getBrandName().contains(brand)).sorted(comparator).collect(Collectors.toList()));
				consoleHelper.printProductList(filteredProductList);
			} else if (in.startsWith("fbn")) {
				String name = in.replace("fbn", "");
				LOGGER.info("extracted name " + name);

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
