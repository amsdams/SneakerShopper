package crawel.helpers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.pojo.Brand;
import crawel.pojo.BrandList;

public class BrandHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandHelper.class);

    private static final BrandHelper instance = new BrandHelper();
    private BrandHelper() { }

    public static BrandHelper getInstance() {
            return instance;
    }
    
	public static String getBrandName(String text, BrandList brandList) {

		List<Brand> brands = new ArrayList<>(brandList.getBrands());
		brands.sort(Brand.BRANDNAMESIZECOMPARATOR);

		String returnBrandName = "unknown";
		for (Brand brand : brands) {

			String brandName = brand.getName();
			List<String> brandAlternatives = BrandCombinations.getBrandCombos(brandName, " X ");
			brandAlternatives.add(brandName);

			for (String s : brandAlternatives) {
				if (text.toUpperCase().contains(s)) {
					returnBrandName = brandName;
					break;
				}
			}
		}
		if ("unknown".equals(returnBrandName)) {
			LOGGER.info("unable to match brand from {}", text);
		}
		return returnBrandName;
	}

	public static String removeBrandName(String text, BrandList brandList) {

		List<Brand> brands = new ArrayList<>(brandList.getBrands());
		brands.sort(Brand.BRANDNAMESIZECOMPARATOR);
		for (Brand brand : brands) {

			String brandName = brand.getName();
			List<String> brandAlternatives = BrandCombinations.getBrandCombos(brandName, " X ");
			brandAlternatives.add(brandName);

			for (String s : brandAlternatives) {
				if (!text.equalsIgnoreCase(s)) {
					text = text.toUpperCase().replaceAll(s, "");

					break;
				}

			}
		}
		return text;
	}

}
