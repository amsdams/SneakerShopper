package crawel.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.stream.Collectors;

import crawel.Crawel;
import crawel.pojo.Action;
import crawel.pojo.ActionList;
import crawel.pojo.BrandList;
import crawel.pojo.CurrencyList;
import crawel.pojo.FileTransferList;
import crawel.pojo.Product;
import crawel.pojo.ProductList;
import crawel.pojo.ShopList;
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;
import crawel.storage.FileTransferListStorage;
import crawel.storage.ProductListStorage;
import crawel.storage.ShopListStorage;
import crawel.storage.SizeListStorage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleHelper {
	public static void goInteractive(Crawel crawler) {
		boolean keepRunning = true;
		ConsoleHelper consoleHelper = new ConsoleHelper();

		ActionList actionList = consoleHelper.getActionList();
		
		ProductList productList = ProductListStorage.get();
		ProductList filteredProductList = new ProductList(productList.getProducts());
		
		
		Comparator<Product> comparator = Product.NewPriceComparator;
		Comparator<Product> secondComparator = Product.NameComparator;

		while (keepRunning) {

			String in = consoleHelper.readLine(consoleHelper.printActionList(actionList));
			log.info("received " + in);
			if (in.startsWith("sbb")) {
				String order = in.replace("sbb", "");
				log.info("will sort by name and order" + order);

				comparator = Product.BrandNameComparator;

				filteredProductList.setProducts(filteredProductList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbn")) {
				String order = in.replace("sbn", "");
				log.info("will sort by name and order" + order);

				comparator = Product.NameComparator;

				filteredProductList.setProducts(filteredProductList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbp")) {
				String order = in.replace("sbp", "");
				log.info("will sort by new price" + order);

				comparator = Product.NewPriceComparator;

				filteredProductList.setProducts(filteredProductList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbs")) {
				String order = in.replace("sbs", "");
				log.info("will sort by shop and order" + order);

				comparator = Product.ShopNameComparator;

				filteredProductList.setProducts(filteredProductList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("sbd")) {
				String order = in.replace("sbd", "");
				log.info("will sort by discount and order" + order);

				comparator = Product.DiscountComparator;

				filteredProductList.setProducts(filteredProductList.getProducts().stream()
						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("fbb")) {
				String brand = in.replace("fbb", "");
				log.info("extracted brand " + brand);

				filteredProductList
						.setProducts(filteredProductList.getProducts().stream().filter(p -> p.getBrandName().contains(brand))
								.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("fbs")) {
				String size = in.replace("fbs", "");
				log.info("extracted size " + size);

				filteredProductList.setProducts(filteredProductList.getProducts().stream()
						.filter(p -> p.getSizesInEU().getSizes().stream().anyMatch(s -> s.getSize().contains(size))

						)

						.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("fbn")) {
				String name = in.replace("fbn", "");
				log.info("extracted name " + name);

				filteredProductList
						.setProducts(filteredProductList.getProducts().stream().filter(p -> p.getName().contains(name))
								.sorted(comparator.thenComparing(secondComparator)).collect(Collectors.toList()));
				ProductListStorage.print(filteredProductList);
			} else if (in.startsWith("reset")) {
				filteredProductList = new ProductList(productList.getProducts());
				
				ProductListStorage.print(filteredProductList);

			} else if (in.startsWith("gp")) {

				filteredProductList = crawler.getShopsProductList();
				ProductListStorage.print(filteredProductList);

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
						currencyList.getCurrencies().stream().sorted().collect(Collectors.toList()));
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

		Action filterBySize = new Action();
		filterBySize.setAction("fbs<size>");
		filterBySize.setDescription("filter by size");
		actionList.getActions().add(filterBySize);

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
			log.warn("could not readline {}", e.getMessage(), e);
			return null;
		}
	}

}
