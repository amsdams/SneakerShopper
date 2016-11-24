package crawel.storage;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.Constants;
import crawel.pojo.Currency;
import crawel.pojo.CurrencyList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CurrencyListStorage {
	private static final String ALL_CURRENCYS_JSON = "allCurrencys.json";

	private static final CurrencyListStorage instance = new CurrencyListStorage();

	public static CurrencyList get() {
		return get(ALL_CURRENCYS_JSON);

	}

	public static CurrencyList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		CurrencyList allCurrencys = new CurrencyList();
		try {
			allCurrencys = mapper.readValue(new File(fileName), CurrencyList.class);
		} catch (Exception e) {
			log.error(Constants.CAUGHT_EXCEPTION_CREATING_NEW, e.getMessage(), e);
			CurrencyList currencyList = new CurrencyList();
			Currency currency = new Currency();
			currency.setName("EURO");
			currency.setSymbol("€");
			currencyList.addCurrency(currency);

			currency = new Currency();
			currency.setName("DOLLAR");
			currency.setSymbol("$");
			currencyList.addCurrency(currency);

			currency = new Currency();
			currency.setName("POUND");
			currency.setSymbol("£");
			currencyList.addCurrency(currency);

			put(currencyList);

		}
		return allCurrencys;

	}

	public static CurrencyListStorage getInstance() {
		return instance;
	}

	public static void print(CurrencyList currencyList) {
		for (Currency currency : currencyList.getCurrencies()) {
			log.info(currency.toString());
		}
		log.info("printed " + currencyList.getCurrencies().size());
	}

	public static void put(CurrencyList brandList) {

		put(brandList, ALL_CURRENCYS_JSON);

	}

	public static void put(CurrencyList currencyList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), currencyList);

		} catch (Exception e){
			log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);

		}

	}

	private CurrencyListStorage() {
	}
}
