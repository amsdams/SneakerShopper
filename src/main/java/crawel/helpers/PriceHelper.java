package crawel.helpers;

import java.math.BigDecimal;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

import org.javamoney.moneta.Money;

import crawel.pojo.Currency;
import crawel.pojo.CurrencyList;

public class PriceHelper {

	private static final PriceHelper instance = new PriceHelper();

	public static Currency getCurrency(String text, CurrencyList currencyList) {
		Currency returnCurrency = new Currency();

		for (Currency currency : currencyList.getCurrencies()) {
			String currencyString = currency.getSymbol();
			if (text.contains(currencyString)) {
				returnCurrency = currency;
			}
		}
		return returnCurrency;
	}

	public static PriceHelper getInstance() {
		return instance;
	}

	public static String removeCurrency(String text, CurrencyList currencyList) {

		for (Currency currency : currencyList.getCurrencies()) {
			String currencyString = currency.getSymbol();
			text = text.replaceAll(currencyString, "");
		}
		return text;
	}

	public static BigDecimal toEuro(BigDecimal price, Currency currenntCurrency, CurrencyList currencyList) {

		CurrencyConversion euroConversion = MonetaryConversions.getConversion("EUR");

		String foundName = "EUR";
		for (Currency currency : currencyList.getCurrencies()) {
			if (currenntCurrency.getName().equals(currency.getName())) {
				foundName = currency.getName();
			}
		}

		MonetaryAmount moneyFound = Money.of(price, foundName);
		MonetaryAmount inEuro = moneyFound.with(euroConversion);
		return BigDecimal.valueOf(inEuro.getNumber().doubleValue());
	}

	private PriceHelper() {
	}
}
