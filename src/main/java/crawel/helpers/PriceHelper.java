package crawel.helpers;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

import org.javamoney.moneta.Money;

import crawel.pojo.Currency;
import crawel.pojo.CurrencyList;

public class PriceHelper {

	public static Currency getCurrency(String text, CurrencyList currencyList) {
		Currency returnCurrency = new Currency();

		for (Currency currency : currencyList.getCurrencys()) {
			String currencyString = currency.getSymbol();
			if (text.toUpperCase().contains(currencyString)) {
				returnCurrency = currency;
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

	public static Double toEuro(Double price, Currency currenntCurrency, CurrencyList currencyList) {
		// ExchangeRateProvider ecbExchangeRateProvider =
		// MonetaryConversions.getExchangeRateProvider("ECB");
		CurrencyConversion euroConversion = MonetaryConversions.getConversion("EUR");

		String foundName = "EUR";
		for (Currency currency : currencyList.getCurrencys()) {
			if (currenntCurrency.getName().equals(currency.getName())) {
				foundName = currency.getName();
			}
		}

		// ExchangeRate rate =
		// ecbExchangeRateProvider.getExchangeRate(foundName, "EUR");
		MonetaryAmount moneyFound = Money.of(price, foundName);
		MonetaryAmount inEuro = moneyFound.with(euroConversion); //  "USD 12.537" (at the time writing)
		return inEuro.getNumber().doubleValue();
	}
}
