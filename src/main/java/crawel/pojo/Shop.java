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
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;

public class Shop {

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

	public Shop() {

		productList = new ProductList();
		brandList = BrandListStorage.get();
		currencyList = CurrencyListStorage.get();

	}

	public void addProductsToList(String url) {

		try {
			LOGGER.info("Fetching {}...", url);

			try (final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER)) {

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

				final HtmlPage page = webClient.getPage(url);
				try {
					for (int i = 0; i < 20; i++) {
						synchronized (page) {
							page.wait(500);
						}
					}
				} catch (Exception e) {
					LOGGER.error("workaround for angular not working {}", e);
				}

				DomNodeList<DomNode> nodes = page.querySelectorAll(this.getProductsSelector());
				for (DomNode node : nodes) {
					Product product = new Product();
					product.setName(this.getProductNameAsString(node, this.getProductNameSelector()));
					
					product.setCurrency(this.getProductCurrencyAsCurrency(node, this.getProductCurrencySelector()));

					
					product.setNewPrice(this.getProductPropertyAsDouble(node, this.getProductNewPriceSelector()));
					product.setOldPrice(this.getProductPropertyAsDouble(node, this.getProductOldPriceSelector()));
					product.setNewPriceInEuro(this.getProductPropertyAsDouble(node, product.getCurrency(), this.getProductNewPriceSelector()));
					product.setOldPriceInEuro(this.getProductPropertyAsDouble(node, product.getCurrency(), this.getProductOldPriceSelector()));
					
					
					product.setBrandName(this.getProductBrandNameAsString(node, this.getProductBrandNameSelector()));
					try {
						String href = this.getProductHrefAsString(node, this.getProductUrlSelector());

						product.setUrl(page.getFullyQualifiedUrl(href).toString());
					} catch (Exception e) {
						LOGGER.error("could not get fully qualified url for node {}", node, e);
					}
					product.setShopName(this.getClass().getName());
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
	};

	public String getNextPageUrl(String url) {
		String nextPageUrl = null;
		try (final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER)) {

			webClient.getOptions().setJavaScriptEnabled(this.getJavaScriptEnabled());
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.waitForBackgroundJavaScript(this.getTimeout());
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

			final HtmlPage page = webClient.getPage(url);

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

			String text = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			text = BrandHelper.removeBrandName(text, brandList);

			text = text.trim();
			productProperty = text;
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
	};

	private void setLimitSelector(Boolean limit) {
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
