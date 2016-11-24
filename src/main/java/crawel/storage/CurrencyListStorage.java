package crawel.storage;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		} catch (IOException e) {
			log.error("could not open file, creating one", e);
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

		} catch (JsonGenerationException e) {
			log.error("could not generate json", e);
		} catch (JsonMappingException e) {
			log.error("could not map json", e);
		} catch (IOException e) {
			log.error("could not write file", e);
		}

	}

	private CurrencyListStorage() {
	}
}
