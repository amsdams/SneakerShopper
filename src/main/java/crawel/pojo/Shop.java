package crawel.pojo;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;

public class Shop implements Comparable<Shop> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Shop.class);

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
			LOGGER.info("Fetching {}...", product.getUrl());
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
				product.setSizes(sizeList);

				product.setSizesInEU(SizeHelper.getSizesInEU(sizeList));
			}
		} catch (Exception e) {
			LOGGER.error("error getting product details with querySelectorAllor {} for baseUrl {}",
					this.getProductDetailsSizesSelector(), product.getUrl());
		}
		return product;
	}

	public void addProductsToList(String url) {

		try {
			LOGGER.info("Fetching {}...", url);

			try (final WebClient webClient = getWebClient()) {
				final HtmlPage page = webClient.getPage(url);

				DomNodeList<DomNode> nodes = page.querySelectorAll(this.getProductsSelector());
				for (DomNode node : nodes) {
					Product product = new Product();
					String name = this.getProductNameAsString(node, this.getProductNameSelector());
					name = BrandHelper.removeBrandName(name, brandList);
					product.setBrandNameRemovedFromName(true);
					product.setName(name);

					product.setCurrency(this.getProductCurrencyAsCurrency(node, this.getProductCurrencySelector()));

					product.setNewPrice(this.getProductPropertyAsDouble(node, this.getProductNewPriceSelector()));
					product.setOldPrice(this.getProductPropertyAsDouble(node, this.getProductOldPriceSelector()));
					product.setNewPriceInEuro(this.getProductPropertyAsDouble(node, product.getCurrency(),
							this.getProductNewPriceSelector()));
					product.setOldPriceInEuro(this.getProductPropertyAsDouble(node, product.getCurrency(),
							this.getProductOldPriceSelector()));

					product.setBrandName(this.getProductBrandNameAsString(node, this.getProductBrandNameSelector()));
					try {
						String href = this.getProductHrefAsString(node, this.getProductUrlSelector());

						product.setUrl(page.getFullyQualifiedUrl(href).toString());
					} catch (Exception e) {
						LOGGER.error("could not get fully qualified url for node {}", node, e);
					}
					product.setShopName(this.getClass().getName());

					// getDetails()
					product = addProductDetailsToProduct(product);

					productList.addProduct(product);
				}

				LOGGER.info("Found {} elements... from {} ", nodes.size(), url);
			}

			if (!this.getLimit()) {
				String nextPageUrl = this.getNextPageUrl(url);
				if (nextPageUrl != null && !nextPageUrl.equals(url)) {
					this.addProductsToList(nextPageUrl);
				}
			}

		} catch (IOException e) {

			LOGGER.error("error getting products with querySelectorAllor {} for baseUrl {}", this.getProductsSelector(),
					this.getBaseUrl(), e);
		}

	}

	@Override
	public int compareTo(Shop o) {
		return this.getBaseUrl().compareTo(o.getBaseUrl());
	}

	public String getBaseUrl() {
		return this.baseUrl;

	}

	public BrandList getBrandList() {
		return brandList;
	}

	public Boolean getJavaScriptEnabled() {
		return javaScriptEnabled;
	}

	private Boolean getLimit() {
		return this.limit;
	}

	public String getNextPageSelector() {
		return nextPageSelector;
	}

	public String getNextPageUrl(String url) {
		String nextPageUrl = null;
		try (final WebClient webClient = getWebClient()) {

			final HtmlPage page = webClient.getPage(url);
			if (page.querySelectorAll(this.getNextPageSelector()) == null) {
				LOGGER.info("no next page found on {}", url);

				return null;
			}
			HtmlElement htmlElement = (HtmlElement) page.querySelectorAll(this.getNextPageSelector()).get(0);

			nextPageUrl = htmlElement.getAttribute("href");
			URL nextPage = page.getFullyQualifiedUrl(nextPageUrl);
			nextPageUrl = nextPage.toString();

		} catch (Exception e) {
			LOGGER.error("Failed to get next page from {}", url, e);
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
			LOGGER.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	public String getProductBrandNameSelector() {
		return productBrandNameSelector;
	}

	private Currency getProductCurrencyAsCurrency(DomNode domNode, String querySelectorAllor) {
		Currency productProperty = null;
		try {

			String text = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();

			text = text.trim();

			productProperty = PriceHelper.getCurrency(text, currencyList);

		} catch (Exception e) {
			LOGGER.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	public String getProductCurrencySelector() {
		return productCurrencySelector;
	}

	private String getProductDetailsSizeSelector() {

		return productDetailsSizeSelector;
	}

	public String getProductDetailsSizesSelector() {
		return productDetailsSizesSelector;
	}

	public String getProductDetailsSizeType() {
		return productDetailsSizeType;
	}

	private String getProductHrefAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {
			HtmlElement htmlElement = (HtmlElement) domNode.querySelectorAll(querySelectorAllor).get(0);

			String text = htmlElement.getAttribute("href");

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	public ProductList getProductList() {
		return this.productList;
	}

	private String getProductNameAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {

			if (domNode.querySelectorAll(querySelectorAllor).size() != 0) {
				String text = domNode.querySelectorAll(querySelectorAllor).get(0).getFirstChild().getTextContent();
				text = text.trim();
				productProperty = text;
			} else {
				LOGGER.warn("could not extract product name with {}", querySelectorAllor);
			}

		} catch (Exception e) {
			LOGGER.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	public String getProductNameSelector() {
		return productNameSelector;
	}

	public String getProductNewPriceSelector() {
		return productNewPriceSelector;
	}

	public String getProductOldPriceSelector() {
		return productOldPriceSelector;
	}

	private Double getProductPropertyAsDouble(DomNode domNode, Currency currency, String querySelectorAllor) {
		Double productProperty = null;
		try {
			String price = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price, currencyList);

			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			productProperty = Double.parseDouble(price);
			productProperty = PriceHelper.toEuro(productProperty, currency, currencyList);
		} catch (Exception e) {
			LOGGER.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return productProperty;
	}

	private Double getProductPropertyAsDouble(DomNode domNode, String querySelectorAllor) {
		Double productProperty = null;
		try {
			String price = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price, currencyList);

			price = price.replaceAll("[^\\d.]", "");
			price = price.trim();
			productProperty = Double.parseDouble(price);
		} catch (Exception e) {
			LOGGER.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
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
			if (!sizeValue.matches(".*\\d+.*")){
				LOGGER.warn("could not find digit in size "+sizeValue +". trying clothing sizes");
				//size  = SizeHelper.getSize(sizeValue, sizeType, new SizesClothing().getSizesClothing());
				//size  =new SizesClothing();
				
			}else{
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
						LOGGER.error("caught exception {}", e.getMessage(), e);
					}
				}
			}
			size.setSizeRaw(productProperty);
			size.setMetric(sizeType);
		} catch (Exception e) {
			LOGGER.error("error getting product property with querySelectorAllor {} for baseUrl {}", querySelectorAllor,
					this.getBaseUrl(), e);
		}
		return size;
	}

	public String getProductsSelector() {
		return productsSelector;
	}

	public String getProductUrlSelector() {
		return productUrlSelector;
	}

	public String getReferrer() {
		return referrer;
	}

	public Boolean getRunnable() {
		return runnable;
	}

	public Integer getTimeout() {
		return timeout;
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

	private void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;

	}

	public void setBrandList(BrandList brandList) {
		this.brandList = brandList;
	}

	public void setJavaScriptEnabled(Boolean javaScriptEnabled) {
		this.javaScriptEnabled = javaScriptEnabled;
	}

	public void setLimit(Boolean limit) {
		this.limit = limit;
	}

	

	public void setNextPageSelector(String nextPageSelector) {
		this.nextPageSelector = nextPageSelector;
	}

	private void setProductBrandNameSelector(String productBrandNameSelector) {
		this.productBrandNameSelector = productBrandNameSelector;

	}

	public void setProductCurrencySelector(String productCurrencySelector) {
		this.productCurrencySelector = productCurrencySelector;
	}

	public void setProductDetailsSizeSelector(String productDetailsSizeSelector) {
		this.productDetailsSizeSelector = productDetailsSizeSelector;
	}

	public void setProductDetailsSizesSelector(String productDetailsSizesSelector) {
		this.productDetailsSizesSelector = productDetailsSizesSelector;
	}

	public void setProductDetailsSizeType(String productDetailsSizeType) {
		this.productDetailsSizeType = productDetailsSizeType;
	}

	public void setProductList(ProductList productList) {
		this.productList = productList;
	}

	private void setProductNameSelector(String productNameSelector) {
		this.productNameSelector = productNameSelector;

	}

	private void setProductNewPriceSelector(String productNewPriceSelector) {
		this.productNewPriceSelector = productNewPriceSelector;

	}

	private void setProductOldPriceSelector(String productOldPriceSelector) {
		this.productOldPriceSelector = productOldPriceSelector;

	}

	public void setProductsSelector(String productsSelector) {
		this.productsSelector = productsSelector;
	}

	private void setProductUrlSelector(String productUrlSelector) {
		this.productUrlSelector = productUrlSelector;

	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public void setRunnable(Boolean runnable) {
		this.runnable = runnable;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		return "Shop [productOldPriceSelector=" + productOldPriceSelector + ", productUrlSelector=" + productUrlSelector
				+ ", productNameSelector=" + productNameSelector + ", productBrandNameSelector="
				+ productBrandNameSelector + ", productNewPriceSelector=" + productNewPriceSelector
				+ ", productCurrencySelector=" + productCurrencySelector + ", nextPageSelector=" + nextPageSelector
				+ ", baseUrl=" + baseUrl + ", productList=" + productList + ", runnable=" + runnable + ", timeout="
				+ timeout + ", productsSelector=" + productsSelector + ", limit=" + limit + ", referrer=" + referrer
				+ ", javaScriptEnabled=" + javaScriptEnabled + "]";
	}

}
