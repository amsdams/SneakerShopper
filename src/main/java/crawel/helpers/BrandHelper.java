package crawel.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandHelper.class);

	

	public static String getBrandName(String text) {
		String brand = "unknown";
		for (String brandString : BRANDS) {
			if (text.toLowerCase().contains(brandString.toLowerCase())) {
				brand = brandString;
			}
		}
		if (brand.equals("unknown")) {
			LOGGER.info("unable to match brand from {}", text);
		}
		return brand;
	}

	public static String removeBrandName(String text) {

		for (String brandString : BRANDS) {
			text = text.replaceAll(brandString, "");
		}
		return text;
	}

}
