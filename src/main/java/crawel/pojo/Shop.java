package crawel.pojo;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import crawel.helpers.BrandHelper;
import crawel.helpers.PriceHelper;
import crawel.helpers.SizeHelper;
import crawel.helpers.sizes.SizesClothing;
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Shop implements Comparable<Shop> {

	private String productOldPriceSelector;

	private String productUrlSelector;

	private String productNameSelector;
	private String productBrandNameSelector;

	private String productNewPriceSelector;
	private String productCurrencySelector;
	private String nextPageSelector;

	private String baseUrl;

	private ProductList productList;
	private BrandList brandList;
	private CurrencyList currencyList;

	private Boolean runnable;

	private Boolean javaScriptEnabled;

	private Integer timeout;

	private String productsSelector;

	private Boolean limit;

	private String referrer = "http://www.google.com";

	private String productDetailsSizeSelector;
	private String productDetailsSizeType;
	private String productDetailsSizesSelector;

	public Shop() {

		productList = new ProductList();
		brandList = BrandListStorage.get();
		currencyList = CurrencyListStorage.get();

	}

	public Product addProductDetailsToProduct(Product product) {
		try {
			log.info("Fetching {}...", product.getUrl());
			try (final WebClient webClient = getWebClient()) {

				final HtmlPage page = webClient.getPage(product.getUrl());
				DomNodeList<DomNode> nodes = page.querySelectorAll(this.getProductDetailsSizesSelector());

				String sizeType = this.getProductDetailsSizeType();

				SizeList sizeList = new SizeList();
				for (DomNode node : nodes) {

					Size size = this.getProductSizeAsSize(node, this.getProductDetailsSizeSelector(), sizeType);
					if (size != null) {

						sizeList.addSize(size);
					}
				}
				product.setSizesRaw(sizeList);

				product.setSizesInEU(SizeHelper.getSizesInEU(sizeList));
			}
		} catch (Exception e) {
			log.error("error getting product details with querySelectorAllor {} for baseUrl {}",
					this.getProductDetailsSizesSelector(), product.getUrl());
		}
		return product;
	}

	public void addProductsToList(String url) {

		try {
			log.info("Fetching {}...", url);

			try (final WebClient webClient = getWebClient()) {
				final HtmlPage page = webClient.getPage(url);

				DomNodeList<DomNode> nodes = page.querySelectorAll(this.getProductsSelector());
				for (DomNode node : nodes) {
					Product product = new Product();
					String name = this.getProductNameAsString(node, this.getProductNameSelector());
					name = BrandHelper.removeBrandName(name, brandList);
					product.setBrandNameRemovedFromName(true);
					product.setName(name);

					product.setCurrencyRaw(this.getProductCurrencyAsCurrency(node, this.getProductCurrencySelector()));

					product.setNewPriceRaw(this.getProductPropertyAsDecimal(node, this.getProductNewPriceSelector()));
					product.setOldPriceRaw(this.getProductPropertyAsDecimal(node, this.getProductOldPriceSelector()));
					product.setNewPriceInEuro(this.getProductPropertyAsDecimal(node, product.getCurrencyRaw(),
							this.getProductNewPriceSelector()));
					product.setOldPriceInEuro(this.getProductPropertyAsDecimal(node, product.getCurrencyRaw(),
							this.getProductOldPriceSelector()));

					product.setBrandName(this.getProductBrandNameAsString(node, this.getProductBrandNameSelector()));
					try {
						String href = this.getProductHrefAsString(node, this.getProductUrlSelector());

						product.setUrl(page.getFullyQualifiedUrl(href).toString());
					} catch (Exception e) {
						log.error("could not get fully qualified url for node {}", node, e);
					}
					product.setShopName(this.getClass().getName());

					// getDetails()
					product = addProductDetailsToProduct(product);

					productList.addProduct(product);
				}

				log.info("Found {} elements... from {} ", nodes.size(), url);
			}

			if (!this.getLimit()) {
				String nextPageUrl = this.getNextPageUrl(url);
				if (nextPageUrl != null && !nextPageUrl.equals(url)) {
					this.addProductsToList(nextPageUrl);
				}
			}

		} catch (IOException e) {

			log.error("error getting products with querySelectorAllor {} for baseUrl {}", this.getProductsSelector(),
					this.getBaseUrl(), e);
		}

	}

	@Override
	public int compareTo(Shop o) {
		return this.getBaseUrl().compareTo(o.getBaseUrl());
	}

	public String getNextPageUrl(String url) {
		String nextPageUrl = null;
		try (final WebClient webClient = getWebClient()) {

			final HtmlPage page = webClient.getPage(url);
			if (page.querySelectorAll(this.getNextPageSelector()) == null) {
				log.info("no next page found on {}", url);

				return null;
			}
			HtmlElement htmlElement = (HtmlElement) page.querySelectorAll(this.getNextPageSelector()).get(0);

			nextPageUrl = htmlElement.getAttribute("href");
			URL nextPage = page.getFullyQualifiedUrl(nextPageUrl);
			nextPageUrl = nextPage.toString();

		} catch (Exception e) {
			log.error("Failed to get next page from {}", url, e);
		}

		return nextPageUrl;
	}

	private String getProductBrandNameAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {

			String text = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			text = BrandHelper.getBrandName(text, brandList);

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			log.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	private Currency getProductCurrencyAsCurrency(DomNode domNode, String querySelectorAllor) {
		Currency productProperty = null;
		try {

			String text = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();

			text = text.trim();

			productProperty = PriceHelper.getCurrency(text, currencyList);

		} catch (Exception e) {
			log.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	private String getProductHrefAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {
			HtmlElement htmlElement = (HtmlElement) domNode.querySelectorAll(querySelectorAllor).get(0);

			String text = htmlElement.getAttribute("href");

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			log.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	private String getProductNameAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {

			if (domNode.querySelectorAll(querySelectorAllor).size() != 0) {
				String text = domNode.querySelectorAll(querySelectorAllor).get(0).getFirstChild().getTextContent();
				text = text.trim();
				productProperty = text;
			} else {
				log.warn("could not extract product name with {}", querySelectorAllor);
			}

		} catch (Exception e) {
			log.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	private BigDecimal getProductPropertyAsDecimal(DomNode domNode, Currency currency, String querySelectorAllor) {
		BigDecimal productProperty = null;
		try {
			String price = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price, currencyList);

			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			productProperty = new BigDecimal(price);

			productProperty = PriceHelper.toEuro(productProperty, currency, currencyList);
		} catch (Exception e) {
			log.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	private BigDecimal getProductPropertyAsDecimal(DomNode domNode, String querySelectorAllor) {
		BigDecimal productProperty = null;
		try {
			String price = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price, currencyList);

			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			productProperty = new BigDecimal(price);
		} catch (Exception e) {
			log.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	private Size getProductSizeAsSize(DomNode domNode, String querySelectorAllor, String sizeType) {
		String productProperty = null;
		String sizeValue = "";
		Size size = new Size();

		try {
			if ("".equals(querySelectorAllor)) {
				sizeValue = domNode.getTextContent();
			} else {
				sizeValue = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();

			}
			sizeValue = sizeValue.trim().toUpperCase();

			/*
			 * 
			 * filteredProductList
			 * .setProducts(productList.getProducts().stream() .filter(p ->
			 * p.getSizesInEU().getSizes().stream() .anyMatch(s
			 * ->s.getSizeRaw().contains(size))
			 * 
			 * 
			 * )
			 * 
			 * .sorted(comparator.thenComparing(secondComparator))
			 * .collect(Collectors.toList()));
			 */
			if (!sizeValue.matches(".*\\d+.*")) {

				// size = SizeHelper.getSize(sizeValue, sizeType, new
				// SizesClothing().getSizesClothing());
				// size =new SizesClothing();
				final String sizeValueCheck = sizeValue;
				if (SizesClothing.getSizesClothing().stream().anyMatch(s -> s.getInLabel().matches(sizeValueCheck))) {
					productProperty = sizeValue;
				} else {
					log.warn("could not use found size " + sizeValue + ". ");
				}

			} else {
				if (sizeType.contains("-")) {
					String[] sizeTypes = sizeType.split("-");

					String[] sizeValues = sizeValue.split("·|-");
					sizeValue = sizeValues[0];
					sizeType = sizeTypes[0];

				}
				sizeValue = sizeValue.replaceAll(",", ".");

				sizeValue = sizeValue.replaceAll("[^\\d.]", "");
				sizeValue = sizeValue.trim();
				if (!"".equals(sizeValue)) {
					try {
						productProperty = sizeValue;
					} catch (NumberFormatException e) {
						log.error("caught exception {}", e.getMessage(), e);
					}
				}
			}
			size.setSizeRaw(productProperty);
			size.setMetric(sizeType);
		} catch (Exception e) {
			log.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return size;
	}

	private WebClient getWebClient() {
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);

		webClient.getOptions().setJavaScriptEnabled(this.getJavaScriptEnabled());
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.waitForBackgroundJavaScript(getTimeout());
		webClient.waitForBackgroundJavaScriptStartingBefore(this.getTimeout());

		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setPopupBlockerEnabled(true);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setTimeout(this.getTimeout());
		webClient.getOptions().setDoNotTrackEnabled(false);
		webClient.getOptions().setUseInsecureSSL(true);
		return webClient;
	}

}
