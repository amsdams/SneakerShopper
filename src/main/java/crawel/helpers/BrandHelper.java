package crawel.helpers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.pojo.Brand;
import crawel.pojo.BrandList;

public class BrandHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandHelper.class);

	public static String getBrandName(String text, BrandList brandList) {
		
		List<Brand> brands = new ArrayList<Brand>(brandList.getBrands());
		brands.sort(Brand.BrandNameSizeComparator);
		
		String returnBrandName = "unknown";
		for (Brand brand: brands) {
		
			
			String brandName = brand.getName();
			List<String> brandAlternatives = BrandCombinations.getBrandCombos(brandName, " X ");
			
			if (text.toUpperCase().contains(brandName)) {
				returnBrandName = brandName;
				break;
			}
			for (String s: brandAlternatives){
				if (text.toUpperCase().contains(s)) {
					returnBrandName = brandName;
					break;
				}
			}
		}
		if (returnBrandName.equals("unknown")) {
			LOGGER.info("unable to match brand from {}", text);
		}
		return returnBrandName;
	}

	public static String removeBrandName(String text, BrandList brandList) {
	
		List<Brand> brands = new ArrayList<Brand>(brandList.getBrands());
		brands.sort(Brand.BrandNameSizeComparator);
		for (Brand brand: brands) {
			
			
			String brandName = brand.getName();
			 
			if (!text.toUpperCase().equals(brandName)){
				text = text.toUpperCase().replaceAll(brandName, "");
				
				break;
				
				
			}
			
		}
		return text;
	}

}
