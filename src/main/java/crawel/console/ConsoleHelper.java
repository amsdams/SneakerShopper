package crawel.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.Crawel;
import crawel.pojo.Action;
import crawel.pojo.ActionList;
import crawel.pojo.BrandList;
import crawel.pojo.CurrencyList;
import crawel.pojo.FileTransferList;
import crawel.pojo.Product;
import crawel.pojo.ProductList;
import crawel.pojo.ShopList;
import crawel.pojo.Size;
import crawel.pojo.SizeList;
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;
import crawel.storage.FileTransferListStorage;
import crawel.storage.ProductListStorage;
import crawel.storage.ShopListStorage;
import crawel.storage.SizeListStorage;

public class ConsoleHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleHelper.class);
	public static void goInteractive(Crawel crawler) {
		boolean keepRunning = true;
		ConsoleHelper consoleHelper = new ConsoleHelper();

		ActionList actionList = consoleHelper.getActionList();
		ProductList filteredProductList = new ProductList();
		ProductList productList = ProductListStorage.get();

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
						.setProducts(productList.getProducts().stream()
								.filter(p -> p.getBrandName().contains(brand))
								.sorted(comparator.thenComparing(secondComparator))
								.collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			}
			else if (in.startsWith("fbs")) {
				String size = in.replace("fbs", "");
				LOGGER.info("extracted size " + size);

				filteredProductList
						.setProducts(productList.getProducts().stream()
								.filter(p -> p.getSizesInEU().getSizes().stream()
										.anyMatch(s ->s.getSizeRaw().contains(size))
										
										
										)
								
								.sorted(comparator.thenComparing(secondComparator))
								.collect(Collectors.toList()));
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

				productList = crawler.getShopsProductList();
				ProductListStorage.print(productList);

			} else if (in.startsWith("sp")) {

				ProductListStorage.put(productList);
			} else if (in.startsWith("ftp")) {

				FileTransferList fileTransferList = FileTransferListStorage.get();
				FileTransferListStorage.putTransfers(fileTransferList);

			} else if (in.startsWith("psl")) {
				ShopListStorage.print(ShopListStorage.get());
			} else if (in.startsWith("pssl")) {
				SizeListStorage.print(SizeListStorage.get());
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
	BufferedReader br;

	PrintStream ps;

	public ConsoleHelper() {
		br = new BufferedReader(new InputStreamReader(System.in));
		ps = System.out;

	}

	public PrintStream format(String format, Object... objects) {
		return ps.format(format, objects);
	}

	public ActionList getActionList() {
		ActionList actionList = new ActionList();

		Action sortProductsByBrand = new Action();
		sortProductsByBrand.setAction("sbb<ASC|DESC>");
		sortProductsByBrand.setDescription("sort by brand");
		actionList.getActions().add(sortProductsByBrand);

		Action sortProductsByName = new Action();
		sortProductsByName.setAction("sbn<ASD|DESC>");
		sortProductsByName.setDescription("sort by name");
		actionList.getActions().add(sortProductsByName);

		Action sortProductsByShop = new Action();
		sortProductsByShop.setAction("sbs<ASD|DESC>");
		sortProductsByShop.setDescription("sort by shop");
		actionList.getActions().add(sortProductsByShop);

		Action sortProductsByNewPrice = new Action();
		sortProductsByNewPrice.setAction("sbp<ASD|DESC>");
		sortProductsByNewPrice.setDescription("sort by (new) price");
		actionList.getActions().add(sortProductsByNewPrice);

		Action sortProductsByDiscount = new Action();
		sortProductsByDiscount.setAction("sbd<ASD|DESC>");
		sortProductsByDiscount.setDescription("sort by discount");
		actionList.getActions().add(sortProductsByDiscount);

		Action filterByBrand = new Action();
		filterByBrand.setAction("fbb<brand>");
		filterByBrand.setDescription("filter by brand");
		actionList.getActions().add(filterByBrand);

		Action filterByName = new Action();
		filterByName.setAction("fbn<name>");
		filterByName.setDescription("filter by name");
		actionList.getActions().add(filterByName);

		Action exit = new Action();
		exit.setAction("bye");
		exit.setDescription("exits the program");
		actionList.getActions().add(exit);

		Action pssl = new Action();
		pssl.setAction("pssl");
		pssl.setDescription("print shoe size list");
		actionList.getActions().add(pssl);

		Action psl = new Action();
		psl.setAction("psl");
		psl.setDescription("print shop list");
		actionList.getActions().add(psl);

		Action pbl = new Action();
		pbl.setAction("pbl");
		pbl.setDescription("print brand list");
		actionList.getActions().add(pbl);

		Action ppl = new Action();
		ppl.setAction("ppl");
		ppl.setDescription("print product list");
		actionList.getActions().add(ppl);

		Action pcl = new Action();
		pcl.setAction("pcl");
		pcl.setDescription("print currency list");
		actionList.getActions().add(pcl);

		Action pft = new Action();
		pft.setAction("pftl");
		pft.setDescription("print file transfer list");
		actionList.getActions().add(pft);

		Action gp = new Action();
		gp.setAction("gp");
		gp.setDescription("get products");
		actionList.getActions().add(gp);

		Action sp = new Action();
		sp.setAction("sp");
		sp.setDescription("save products");
		actionList.getActions().add(sp);

		Action ftp = new Action();
		ftp.setAction("ftp");
		ftp.setDescription("save products on ftp");
		actionList.getActions().add(ftp);

		return actionList;
	}

	public String printActionList(ActionList actionList) {

		StringBuilder question = new StringBuilder();
		question.append("What do you want to do?\r\n");
		for (Action action : actionList.getActions()) {
			question.append("type " + action.getAction() + " to " + action.getDescription() + "\r\n");
		}

		return question.toString();

	}

	public String readLine(String out) {
		ps.format(out);
		try {
			return br.readLine();
		} catch (IOException e) {
			LOGGER.warn("could not readline {}", e.getMessage(), e);
			return null;
		}
	}

}
