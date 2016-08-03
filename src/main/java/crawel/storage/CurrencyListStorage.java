package crawel.storage;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.Currency;
import crawel.pojo.CurrencyList;

public class CurrencyListStorage {
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyListStorage.class);

	public static CurrencyList get() {
		ObjectMapper mapper = new ObjectMapper();
		CurrencyList allCurrencys = new CurrencyList();
		try {
			allCurrencys = mapper.readValue(new File("allCurrencys.json"), CurrencyList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file, creating one", e);
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
	public static void print(CurrencyList currencyList) {
		for (Currency currency : currencyList.getCurrencys()) {
			LOGGER.info(currency.toString());
		}
		LOGGER.info("printed "+ currencyList.getCurrencys().size() );
	}

	public static void put(CurrencyList currencyList) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File("allCurrencys.json"), currencyList);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
}
