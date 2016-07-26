package crawel.helpers;

public class PriceHelper {
	public static String[] CURRENCIES = { "€", "$", "£" };

	public static String getCurrency(String text) {
		String currency = "unknown";
		for (String currencyString : CURRENCIES) {
			if (text.toLowerCase().contains(currencyString.toLowerCase())) {
				currency = currencyString;
			}
		}
		return currency;
	}

	public static String removeCurrency(String text) {

		for (String currencyString : CURRENCIES) {
			text = text.replaceAll(currencyString, "");
		}
		return text;
	}
}
