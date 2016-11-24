package crawel.helpers;

import java.util.ArrayList;
import java.util.List;

import crawel.Constants;
import crawel.pojo.Brand;
import crawel.pojo.BrandList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrandHelper {

	private static final BrandHelper instance = new BrandHelper();

	public static String getBrandName(String text, BrandList brandList) {
		

		List<Brand> brands = new ArrayList<>(brandList.getBrands());
		brands.sort(Brand.BRANDNAMESIZECOMPARATOR);

		String returnBrandName = Constants.UNKOWN_BRAND;
		for (Brand brand : brands) {

			String brandName = brand.getName();
			brandName  = TextHelper.sanitize(brandName);

			List<String> brandAlternatives = BrandCombinations.getBrandCombos(brandName, " X ");
			brandAlternatives.add(brandName);

			for (String s : brandAlternatives) {
				if (text.contains(s)) {
					returnBrandName = brandName;
					break;
				}
			}
		}
		if (Constants.UNKOWN_BRAND.equals(returnBrandName)) {
			log.info(Constants.NOT_FOUND_BRAND_FROM, text);
		}
		return returnBrandName.trim();
	}

	public static BrandHelper getInstance() {
		return instance;
	}

	public static String removeBrandName(String text, BrandList brandList) {
		

		List<Brand> brands = new ArrayList<>(brandList.getBrands());
		brands.sort(Brand.BRANDNAMESIZECOMPARATOR);
		for (Brand brand : brands) {

			String brandName = brand.getName();
			brandName  = TextHelper.sanitize(brandName);
			

			List<String> brandAlternatives = BrandCombinations.getBrandCombos(brandName, " X ");
			brandAlternatives.add(brandName);

			for (String s : brandAlternatives) {
				if (!text.equals(s)) {
					text = text.replaceAll(s, "");

					break;
				}

			}
		}
		return TextHelper.sanitize(text);
	}

	private BrandHelper() {
	}

}
