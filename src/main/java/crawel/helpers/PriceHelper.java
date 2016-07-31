package crawel.helpers;

import crawel.pojo.Currency;
import crawel.pojo.CurrencyList;

public class PriceHelper {
	/*
	 * public static String[] CURRENCIES = { "€", "$", "£" };
	 */

	public static String getCurrency(String text, CurrencyList currencyList) {
		String returnCurrency = "unknown";
		for (Currency currency : currencyList.getCurrencys()) {
			String currencyString = currency.getSymbol();
			if (text.toLowerCase().contains(currencyString.toLowerCase())) {
				returnCurrency = currencyString;
			}
		}
		return returnCurrency;
	}

	public static String removeCurrency(String text, CurrencyList currencyList) {

		for (Currency currency : currencyList.getCurrencys()) {
			String currencyString = currency.getSymbol();
			text = text.replaceAll(currencyString, "");
		}
		return text;
	}
}
