package crawel.crawler;

import java.math.BigDecimal;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import crawel.Constants;
import crawel.helpers.BrandHelper;
import crawel.helpers.PriceHelper;
import crawel.helpers.TextHelper;
import crawel.pojo.Currency;
import crawel.pojo.Product;
import crawel.pojo.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data

public class PageCrawlerOverview {

	public void addProductsToList(PageCrawlerClient client, HtmlPage page, Shop shop) {

		try {

			DomNodeList<DomNode> nodes = page.querySelectorAll(shop.getProductsSelector());
			for (DomNode node : nodes) {

				String name = this.getProductNameAsString(node, shop.getProductNameSelector());

				name = BrandHelper.removeBrandName(name);

				Currency currecyRaw = this.getProductCurrencyAsCurrency(node, shop.getProductCurrencySelector());

				BigDecimal newPriceRaw = this.getProductPropertyAsDecimal(node, shop.getProductNewPriceSelector());
				BigDecimal oldPriceRaw = this.getProductPropertyAsDecimal(node, shop.getProductOldPriceSelector());

				BigDecimal newPriceInEuro = this.getProductPropertyAsDecimal(node, currecyRaw,
						shop.getProductNewPriceSelector());

				BigDecimal oldPriceInEuro = this.getProductPropertyAsDecimal(node, currecyRaw,
						shop.getProductOldPriceSelector());

				String brandName = this.getProductBrandNameAsString(node, shop.getProductBrandNameSelector());

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
					String href = this.getProductHrefAsString(node, shop.getProductUrlSelector());

					product.setUrl(page.getFullyQualifiedUrl(href).toString());
				} catch (Exception e) {
					if (log.isErrorEnabled()) {
						log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
					}
				}
				product.setShopName(shop.getBaseUrl());

				if (shop.getDetailsEnabled() != null && shop.getDetailsEnabled()) {
					PageCrawlerDetail crawelDetailsWeb = new PageCrawlerDetail();

					product = crawelDetailsWeb.addProductDetailsToProduct(product, client, page, shop);
				}
				shop.productList.addProduct(product);
			}
			log.info("Found {} elements... from {} ", nodes.size(), page.getWebResponse().getWebRequest().getUrl());

			if (!shop.getLimitEnabled()) {
				String nextPageUrl = this.getNextPageUrl(client, page, shop);
				if (nextPageUrl != null && !nextPageUrl.equals(shop.getBaseUrl())) {
					HtmlPage nextPage = client.getHtmlPage(nextPageUrl, shop);

					PageCrawlerOverview next = new PageCrawlerOverview();

					next.addProductsToList(client, nextPage, shop);
				}
			}

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}

	}

	public String getNextPageUrl(PageCrawlerClient client, HtmlPage page, Shop shop) {
		String nextPageUrl = null;
		try {
			DomNodeList<DomNode> nodes = page.querySelectorAll(shop.getNextPageSelector());
			if (!nodes.isEmpty()) {
				HtmlElement htmlElement = (HtmlElement) page.querySelectorAll(shop.getNextPageSelector()).get(0);

				nextPageUrl = htmlElement.getAttribute("href");
				URL nextPage = page.getFullyQualifiedUrl(nextPageUrl);
				nextPageUrl = nextPage.toString();
			}

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}

		return nextPageUrl;
	}

	private String getProductBrandNameAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {

			productProperty = getText(domNode, querySelectorAllor);
			productProperty = TextHelper.sanitize(productProperty);

			productProperty = BrandHelper.getBrandName(productProperty);

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private String getText(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		DomNodeList<DomNode> nodes = domNode.querySelectorAll(querySelectorAllor);
		if (!nodes.isEmpty()) {
			productProperty = nodes.get(0).getTextContent();

		}
		if (StringUtils.isEmpty(productProperty)) {
			log.warn("could not get text with {} from {}", querySelectorAllor, domNode.getPage().getUrl());
		}
		return productProperty;
	}

	private String getFirstText(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		DomNodeList<DomNode> nodes = domNode.querySelectorAll(querySelectorAllor);
		if (!nodes.isEmpty()) {
			String text = nodes.get(0).getFirstChild().getTextContent();
			productProperty = text;
		}

		if (StringUtils.isEmpty(productProperty)) {
			log.warn("could not get text with {} from {}", querySelectorAllor, domNode.getPage().getUrl());
		}
		return productProperty;
	}

	private Currency getProductCurrencyAsCurrency(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		Currency currency = null;
		try {

			productProperty = getText(domNode, querySelectorAllor);
			productProperty = TextHelper.sanitize(productProperty);

			currency = PriceHelper.getCurrency(productProperty);

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return currency;
	}

	private String getProductHrefAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {
			// productProperty = getText(domNode, querySelectorAllor);

			DomNodeList<DomNode> nodes = domNode.querySelectorAll(querySelectorAllor);
			if (!nodes.isEmpty()) {
				HtmlElement htmlElement = (HtmlElement) nodes.get(0);

				String text = htmlElement.getAttribute("href");

				// text = TextHelper.sanitize(text);

				productProperty = text;
			}

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private String getProductNameAsString(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		try {

			productProperty = getText(domNode, querySelectorAllor);

			// nodes.get(0).getFirstChild().getTextContent();
			productProperty = TextHelper.sanitize(productProperty);

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private BigDecimal getProductPropertyAsDecimal(DomNode domNode, Currency currency, String querySelectorAllor) {
		BigDecimal productProperty = null;
		try {

			String price = getText(domNode, querySelectorAllor);

			price = TextHelper.sanitize(price);
			// TODO extract Currency
			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price);

			price = price.replaceAll("[^\\d.]", "");

			productProperty = new BigDecimal(price);

			productProperty = PriceHelper.toEuro(productProperty, currency);

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

	private BigDecimal getProductPropertyAsDecimal(DomNode domNode, String querySelectorAllor) {
		BigDecimal productProperty = null;
		try {

			String price = getText(domNode, querySelectorAllor);
			price = TextHelper.sanitize(price);

			price = price.replaceAll(",", ".");

			price = PriceHelper.removeCurrency(price);

			price = price.replaceAll("[^\\d.]", "");
			productProperty = new BigDecimal(price);

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return productProperty;
	}

}
