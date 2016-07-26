package crawel.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.Shopper;

public class BrandHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandHelper.class);
	public static String getBrandName(String text) {
		String brand = "unknown";
		for (String brandString : BRANDS) {
			if (text.toLowerCase().contains(brandString.toLowerCase())) {
				brand = brandString;
			}
		}
		if (brand.equals("unknown")){
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
	public static String[] BRANDS = { "Nike", "Reebok", "Adidas", "Puma", "Converse", "Diadora", "Ben Sherman",
			"Le Coq Sportif", "New Balance", "Asics", "Stussy", "Saucony", "Publish", "Lacoste", "Karhu", "Hummel", "Carhartt", "Jordan", "Staple", "Ransom", "KangaROOS", "Vans", "Poler", "Ucon", 
			"Undefeated", "Beastin", "OPM", "New Era", "LOOKYLOOKY", "ESPERANDO", "NVRMRE", "Jason Markk","43einhalb", "On"};

}
