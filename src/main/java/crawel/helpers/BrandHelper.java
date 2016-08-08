package crawel.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.pojo.Brand;
import crawel.pojo.BrandList;

public class BrandHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandHelper.class);

	public static String getBrandName(String text, BrandList brandlist) {
		brandlist.getBrands().sort(Brand.BrandNameSizeComparator);
		String returnBrandName = "unknown";
		for (Brand brand : brandlist.getBrands()) {
			String brandName = brand.getName();
			if (text.toUpperCase().contains(brandName)) {
				returnBrandName = brandName;
			}
		}
		if (returnBrandName.equals("unknown")) {
			LOGGER.info("unable to match brand from {}", text);
		}
		return returnBrandName;
	}

	public static String removeBrandName(String text, BrandList brandlist) {
		brandlist.getBrands().sort(Brand.BrandNameSizeComparator);
		for (Brand brand : brandlist.getBrands()) {
			String brandName = brand.getName();
			text = text.toUpperCase().replaceAll(brandName, "");
		}
		return text;
	}

}
