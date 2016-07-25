package crawel.helpers;

public class BrandHelper {
	public static String getBrandName(String text) {
		String brand = "unknown";
		for (String brandString : BRANDS) {
			if (text.toLowerCase().contains(brandString.toLowerCase())) {
				brand = brandString;
			}
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
			"Le Coq Sportif", "New Balance", "Asics", "Stussy", "Saucony", "Publish", "Lacoste", "Karhu", "Hummel", "Carhartt", "Air Jordan", "Staple"};

}
