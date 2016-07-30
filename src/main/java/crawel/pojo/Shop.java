package crawel.pojo;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.helpers.BrandHelper;
import crawel.helpers.PriceHelper;
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;

public class Shop {

	@Override
	public String toString() {
		return "Shop [productOldPriceSelector=" + productOldPriceSelector + ", productUrlSelector=" + productUrlSelector
				+ ", productNameSelector=" + productNameSelector + ", productBrandNameSelector="
				+ productBrandNameSelector + ", productNewPriceSelector=" + productNewPriceSelector
				+ ", productCurrencySelector=" + productCurrencySelector + ", nextPageSelector=" + nextPageSelector
				+ ", baseUrl=" + baseUrl + ", productList=" + productList + ", runnable=" + runnable + ", timeout="
				+ timeout + ", productsSelector=" + productsSelector + ", limit=" + limit + ", userAgent=" + userAgent
				+ ", referrer=" + referrer + "]";
	}

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

	private Integer timeout;

	public void setLimit(Boolean limit) {
		this.limit = limit;
	}

	private String productsSelector;

	private Boolean limit;

	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0";

	private String referrer = "http://www.google.com";

	public Shop() {

		productList = new ProductList();
		brandList = BrandListStorage.get();
		currencyList = CurrencyListStorage.get();

	}

	public void addProductsToList(String url) {

		try {
			LOGGER.info("Fetching {}...", url);

			Document doc = Jsoup.connect(url).ignoreContentType(true).userAgent(this.getUserAgent())
					.referrer(this.getReferrer()).followRedirects(true).timeout(getTimeout()).get();

			Elements productElements = doc.select(this.getProductsSelector());
			for (Element productElement : productElements) {
				Product product = new Product();

				product.setName(this.getProductNameAsString(productElement, this.getProductNameSelector()));
				product.setNewPrice(this.getProductPropertyAsDouble(productElement, this.getProductNewPriceSelector()));
				product.setOldPrice(this.getProductPropertyAsDouble(productElement, this.getProductOldPriceSelector()));
				product.setBrandName(
						this.getProductBrandNameAsString(productElement, this.getProductBrandNameSelector()));
				product.setUrl(this.getProductUrlAsString(productElement, this.getProductUrlSelector()));
				product.setShopName(this.getClass().getName());
				product.setCurrency(this.getProductCurrencyAsString(productElement, this.getProductCurrencySelector()));
				productList.addProduct(product);
			}
			if (!this.getLimit()) {
				String nextPageUrl = this.getNextPageUrl(url);
				if (nextPageUrl != null && !nextPageUrl.equals(url)) {
					this.addProductsToList(nextPageUrl);
				}
			}

			LOGGER.info("Found {} products... from {} ", productElements.size(), url);

		} catch (IOException e) {

			LOGGER.error("error getting products with selector {} for baseUrl {}", this.getProductsSelector(),
					this.getBaseUrl(), e);
		}

	}

	public String getBaseUrl() {
		return this.baseUrl;

	}

	private Boolean getLimit() {
		return this.limit;
	}

	public String getNextPageSelector() {
		return nextPageSelector;
	};

	public String getNextPageUrl(String url) {
		String nextPageUrl = null;

		Document doc;
		try {
			doc = Jsoup.connect(url).ignoreContentType(true).userAgent(this.getUserAgent()).referrer(this.getReferrer())
					.followRedirects(true).timeout(this.getTimeout()).get();
			Element nextLinkElement = doc.select(this.getNextPageSelector()).get(0);
			nextPageUrl = nextLinkElement.attr("abs:href");
		} catch (Exception e) {
			LOGGER.error("failed to get next page ", e);
		}

		return nextPageUrl;
	}

	private String getProductBrandNameAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).get(0).ownText();
			text = BrandHelper.getBrandName(text, brandList);

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {} for baseUrl {}", selector, this.getBaseUrl(),
					e);
		}
		return productProperty;
	}

	public String getProductBrandNameSelector() {
		return productBrandNameSelector;
	}

	private String getProductCurrencyAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).get(0).ownText();
			text = PriceHelper.getCurrency(text, currencyList);

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {} for baseUrl {}", selector, this.getBaseUrl(),
					e);
		}
		return productProperty;
	}

	public String getProductCurrencySelector() {
		return productCurrencySelector;
	}

	public ProductList getProductList() {
		return this.productList;
	}

	private String getProductNameAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).get(0).ownText();
			text = BrandHelper.removeBrandName(text, brandList);

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {} for baseUrl {}", selector, this.getBaseUrl(),
					e);
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

	private Double getProductPropertyAsDouble(Element productElement, String selector) {
		Double productProperty = null;
		try {
			String price = productElement.select(selector).get(0).ownText();
			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price, currencyList);
			price = price.trim();
			productProperty = Double.parseDouble(price);

		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {} for baseUrl {}", selector, this.getBaseUrl(),
					e);
		}
		return productProperty;
	}

	private String getProductPropertyAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).get(0).ownText();
			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {} for baseUrl {}", selector, this.getBaseUrl(),
					e);
		}
		return productProperty;
	}

	public String getProductsSelector() {
		return productsSelector;
	}

	private String getProductUrlAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).attr("abs:href");
			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {} for baseUrl {}", selector, this.getBaseUrl(),
					e);
		}
		return productProperty;
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

	public String getUserAgent() {
		return userAgent;
	}

	private void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;

	}

	private void setLimitSelector(Boolean limit) {
		this.limit = limit;

	}

	public void setNextPageSelector(String nextPageSelector) {
		this.nextPageSelector = nextPageSelector;
	}

	private void setProductBrandNameSelector(String productBrandNameSelector) {
		this.productBrandNameSelector = productBrandNameSelector;

	};

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

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public BrandList getBrandList() {
		return brandList;
	}

	public void setBrandList(BrandList brandList) {
		this.brandList = brandList;
	}

}
