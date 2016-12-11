package crawel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import crawel.console.ConsoleHelper;
import crawel.pojo.ProductList;
import crawel.pojo.Shop;
import crawel.pojo.ShopList;
import crawel.storage.ProductListStorage;
import crawel.storage.ShopListStorage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Crawel {
	private static final int MYTHREADS = 40;

	public static void main(String[] args) {
		Crawel crawel = new Crawel();
		CmdLineParser parser = new CmdLineParser(crawel);

		try {
			parser.parseArgument(args);
			if (crawel.writeFile) {

				ProductList productList = crawel.getShopsProductList();
				ProductListStorage.print(productList);
				ProductListStorage.put(productList);
			}

			if (crawel.openFile) {

				ProductList productList = ProductListStorage.get();

				ProductListStorage.print(productList);

			}
			if (crawel.isInterative) {
				ConsoleHelper.goInteractive(crawel);
			}

		} catch (CmdLineException e) {
			// handling of wrong arguments
			if (log.isWarnEnabled()) {
				log.warn("could not read commandline {}", e.getMessage(), e);
			}
			parser.printUsage(System.err);
		}

	}

	@Option(name = "--interactive", usage = "start interactive")
	private Boolean isInterative = false;

	@Option(name = "--write", usage = "write file")
	private Boolean writeFile = false;

	@Option(name = "--open", usage = "open file")
	private Boolean openFile = false;

	public ProductList getShopsProductList() {
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);

		ShopList shopList = ShopListStorage.get();

		for (Shop shop : shopList.getShops()) {

			if (shopList.getLimitEnabled()) {
				shop.setLimitEnabled(true);
				log.info("will not get all pages");
			}

			if (shopList.getDetailsEnabled()) {
				shop.setDetailsEnabled(true);
				log.info("will get all details");

			}

			if (shopList.getJavaScriptEnabled()) {
				shop.setJavaScriptEnabled(true);
				log.info("will use javascript");

			}
			
			Runnable worker = new ShopRunner(shop);

			executor.execute(worker);

		}

		executor.shutdown();
		// Wait until all threads are finish
		while (!executor.isTerminated())

		{

		}
		if (log.isInfoEnabled()) {
			log.info("\nFinished all threads");
		}

		ProductList allProducts = new ProductList();
		for (Shop shop : shopList.getShops()) {
			allProducts.getProducts().addAll(shop.getProductList().getProducts());

		}
		return allProducts;
	}

}
