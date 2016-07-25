package crawel;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.helpers.BrandHelper;
import crawel.helpers.PriceHelper;

public abstract class Shop {

	private static final Logger LOGGER = LoggerFactory.getLogger(Shop.class);

	private String productOldPriceSelector;

	private String productUrlSelector;
	private String productNameSelector;

	private String productBrandNameSelector;
	private String productNewPriceSelector;
	private String nextPageSelector;

	private String baseUrl;
	private ProductList productList;

	private Boolean runnable;

	private Integer timeout;
	private String productsSelector;

	public Shop() {
		try {
			ResourceBundle resourceBundle = ResourceBundle.getBundle(this.getClass().getName());
			this.setRunnable(Boolean.parseBoolean(resourceBundle.getString("runnable")));
			this.setBaseUrl(resourceBundle.getString("baseUrl"));
			this.setTimeout(Integer.parseInt(resourceBundle.getString("timeout")));
			this.setProductsSelector(resourceBundle.getString("productsSelector"));
			this.setNextPageSelector(resourceBundle.getString("nextPageSelector"));
			this.setProductNewPriceSelector(resourceBundle.getString("productNewPriceSelector"));
			this.setProductOldPriceSelector(resourceBundle.getString("productOldPriceSelector"));
			this.setProductBrandNameSelector(resourceBundle.getString("productBrandNameSelector"));
			this.setProductNameSelector(resourceBundle.getString("productNameSelector"));
			this.setProductUrlSelector(resourceBundle.getString("productUrlSelector"));
			this.setLimitSelector(Boolean.parseBoolean(resourceBundle.getString("limit")));
			Enumeration<String> resourceBundleKeys = resourceBundle.getKeys();
			while (resourceBundleKeys.hasMoreElements()) {
				String resourceBundleKey = resourceBundleKeys.nextElement();
				String value = resourceBundle.getString(resourceBundleKey);
				LOGGER.info(resourceBundleKey + ": " + value);
			}
			productList = new ProductList();
		} catch (Exception e) {
			LOGGER.error("Error retrieving properties file: {}", e);
		}
	}

	private Boolean limit;

	private void setLimitSelector(Boolean limit) {
		this.limit = limit;

	}

	private Boolean getLimit() {
		return this.limit;
	}

	public String getBaseUrl() {
		return this.baseUrl;

	}

	public String getProductBrandNameSelector() {
		return productBrandNameSelector;
	}

	public String getNextPageUrl(String url) {
		String nextPageUrl = null;

		Document doc;
		try {
			doc = Jsoup.connect(url).timeout(20 * 1000).get();
			Element nextLinkElement = doc.select(this.getNextPageSelector()).get(0);
			nextPageUrl = nextLinkElement.attr("abs:href");
		} catch (Exception e) {
			LOGGER.error("failed to get next page ", e);
		}

		return nextPageUrl;
	}

	public ProductList getProductList() {
		return this.productList;
	}

	public void addProductsToList(String url) {

		try {
			LOGGER.info("Fetching {}...", url);

			Document doc = Jsoup.connect(url).timeout(getTimeout()).get();

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
				product.setCurrency(this.getProductCurrencyAsString(productElement, this.getProductNewPriceSelector()));
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

			LOGGER.error("error getting products with selector {} ", this.getProductsSelector(), e);
		}

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
			price = PriceHelper.removeCurrency(price);
			price = price.trim();
			productProperty = Double.parseDouble(price);

		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {}", selector, e);
		}
		return productProperty;
	}

	private String getProductCurrencyAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).get(0).ownText();
			text = PriceHelper.getCurrency(text);

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {}", selector, e);
		}
		return productProperty;
	}

	private String getProductNameAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).get(0).ownText();
			text = BrandHelper.removeBrandName(text);

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {}", selector, e);
		}
		return productProperty;
	}

	private String getProductBrandNameAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).get(0).ownText();
			text = BrandHelper.getBrandName(text);

			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {}", selector, e);
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
			LOGGER.error("error getting product property with selector {}", selector, e);
		}
		return productProperty;
	}

	private String getProductUrlAsString(Element productElement, String selector) {
		String productProperty = null;
		try {

			String text = productElement.select(selector).attr("abs:href");
			text = text.trim();
			productProperty = text;
		} catch (Exception e) {
			LOGGER.error("error getting product property with selector {}", selector, e);
		}
		return productProperty;
	}

	public String getProductsSelector() {
		return productsSelector;
	}

	public String getProductUrlSelector() {
		return productUrlSelector;
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

	private void setProductBrandNameSelector(String productBrandNameSelector) {
		this.productBrandNameSelector = productBrandNameSelector;

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
	};

	private void setProductUrlSelector(String productUrlSelector) {
		this.productUrlSelector = productUrlSelector;

	}

	public void setRunnable(Boolean runnable) {
		this.runnable = runnable;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getNextPageSelector() {
		return nextPageSelector;
	}

	public void setNextPageSelector(String nextPageSelector) {
		this.nextPageSelector = nextPageSelector;
	}

}
