package crawel.crawler;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import crawel.Constants;
import crawel.helpers.SizeHelper;
import crawel.helpers.TextHelper;
import crawel.helpers.sizes.SizesClothing;
import crawel.pojo.Product;
import crawel.pojo.Shop;
import crawel.pojo.Size;
import crawel.pojo.SizeList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data

public class PageCrawlerDetail {

	public Size getProductSizeAsSize(DomNode domNode, String querySelectorAllor, String sizeType) {
		String productProperty = null;
		String sizeValue = "";
		Size size = new Size("", "");

		try {
			if ("".equals(querySelectorAllor)) {
				sizeValue = domNode.getTextContent();
			} else {
				sizeValue = getText(domNode, querySelectorAllor);

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
						if (log.isErrorEnabled()) {
							log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
						}
					}
				}
			}
			if (productProperty != null) {
				size.setSize(productProperty);
				size.setMetric(sizeType);
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return size;
	}

	public Product addProductDetailsToProduct(Product product, PageCrawlerClient client, HtmlPage page, Shop shop) {
		try {

			DomNodeList<DomNode> nodes = page.querySelectorAll(shop.getProductDetailsSizesSelector());

			String sizeType = shop.getProductDetailsSizeType();

			SizeList sizeList = new SizeList();
			for (DomNode node : nodes) {

				Size size = this.getProductSizeAsSize(node, shop.getProductDetailsSizeSelector(), sizeType);
				if (size != null) {

					sizeList.addSize(size);
				}
			}
			product.setSizesRaw(sizeList);

			product.setSizesInEU(SizeHelper.getSizesInEU(sizeList));

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return product;
	}

	private String getText(DomNode domNode, String querySelectorAllor) {
		String productProperty = null;
		DomNodeList<DomNode> nodes = domNode.querySelectorAll(querySelectorAllor);
		if (!nodes.isEmpty()) {
			productProperty = nodes.get(0).getTextContent();

		}
		if (productProperty == null) {
			log.warn("could not get text with {} from {}", querySelectorAllor, domNode);
		}
		return productProperty;
	}
}
