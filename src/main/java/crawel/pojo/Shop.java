package crawel.pojo;

import java.math.BigDecimal;
import java.net.URL;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import crawel.Constants;
import crawel.helpers.BrandHelper;
import crawel.helpers.PriceHelper;
import crawel.helpers.SizeHelper;
import crawel.helpers.TextHelper;
import crawel.helpers.sizes.SizesClothing;
import crawel.storage.BrandListStorage;
import crawel.storage.CurrencyListStorage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)

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
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
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
					
					String name = this.getProductNameAsString(node, this.getProductNameSelector());
					name = BrandHelper.removeBrandName(name, brandList);

					Currency currecyRaw = this.getProductCurrencyAsCurrency(node, this.getProductCurrencySelector());

					
					BigDecimal newPriceRaw = this.getProductPropertyAsDecimal(node, this.getProductNewPriceSelector());
					BigDecimal oldPriceRaw = this.getProductPropertyAsDecimal(node, this.getProductOldPriceSelector());

					BigDecimal newPriceInEuro = this.getProductPropertyAsDecimal(node, currecyRaw,
							this.getProductNewPriceSelector());

					BigDecimal oldPriceInEuro = this.getProductPropertyAsDecimal(node, currecyRaw,
							this.getProductOldPriceSelector());

					String brandName = this.getProductBrandNameAsString(node, this.getProductBrandNameSelector());

					Product product = new Product();
					product.setBrandName(brandName);
					product.setBrandNameRemovedFromName(true);
					product.setName(name);
					
					product.setCurrencyRaw(currecyRaw);
					product.setNewPriceRaw(newPriceRaw);
					product.setOldPriceRaw(oldPriceRaw);

					product.setNewPriceInEuro(newPriceInEuro);

					product.setOldPriceInEuro(oldPriceInEuro);

					
					try {
						String href = this.getProductHrefAsString(node, this.getProductUrlSelector());

						product.setUrl(page.getFullyQualifiedUrl(href).toString());
					} catch (Exception e) {
						if (log.isErrorEnabled()){
							log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
						}
					}
					product.setShopName(this.getClass().getName());

				
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

		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
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
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}

		return nextPageUrl;
	}

	private String getProductBrandNameAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {

			String text = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			text = TextHelper.sanitize(text);

			text = BrandHelper.getBrandName(text, brandList);

			productProperty = text;
		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private Currency getProductCurrencyAsCurrency(DomNode domNode, String querySelectorAllor) {
		Currency productProperty = null;
		try {

			String text = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();

			text = TextHelper.sanitize(text);

			productProperty = PriceHelper.getCurrency(text, currencyList);

		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private String getProductHrefAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {
			HtmlElement htmlElement = (HtmlElement) domNode.querySelectorAll(querySelectorAllor).get(0);

			String text = htmlElement.getAttribute("href");

			//text = TextHelper.sanitize(text);

			productProperty = text;
		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private String getProductNameAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {

			if (domNode.querySelectorAll(querySelectorAllor).size() != 0) {
				String text = domNode.querySelectorAll(querySelectorAllor).get(0).getFirstChild().getTextContent();
				text = TextHelper.sanitize(text);

				productProperty = text;
			} else {
				log.warn("could not extract product name with {}", querySelectorAllor);
			}

		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private BigDecimal getProductPropertyAsDecimal(DomNode domNode, Currency currency, String querySelectorAllor) {
		BigDecimal productProperty = null;
		try {
			String price = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			price = TextHelper.sanitize(price);
//TODO extract Currency 
			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price, currencyList);

			price = price.replaceAll("[^\\d.]", "");

			productProperty = new BigDecimal(price);

			productProperty = PriceHelper.toEuro(productProperty, currency, currencyList);
		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private BigDecimal getProductPropertyAsDecimal(DomNode domNode, String querySelectorAllor) {
		BigDecimal productProperty = null;
		try {
			String price = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();
			price = TextHelper.sanitize(price);

			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price, currencyList);

			price = price.replaceAll("[^\\d.]", "");
			productProperty = new BigDecimal(price);
		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private Size getProductSizeAsSize(DomNode domNode, String querySelectorAllor, String sizeType) {
		String productProperty = null;
		String sizeValue = "";
		Size size = new Size("", "");

		try {
			if ("".equals(querySelectorAllor)) {
				sizeValue = domNode.getTextContent();
			} else {
				sizeValue = domNode.querySelectorAll(querySelectorAllor).get(0).getTextContent();

			}
			sizeValue = TextHelper.sanitize(sizeValue);

			if (!sizeValue.matches(".*\\d+.*")) {

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

				if (!"".equals(sizeValue)) {
					try {
						productProperty = sizeValue;
					} catch (NumberFormatException e) {
						if (log.isErrorEnabled()){
							log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
						}
					}
				}
			}
			if (productProperty!= null){
				size.setSize(productProperty);
				size.setMetric(sizeType);
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
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
