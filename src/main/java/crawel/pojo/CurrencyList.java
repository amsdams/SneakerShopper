package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrencyList {
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyList.class);

	private List<Currency> currencies;

	public CurrencyList(List<Currency> collect) {
		this.currencies = collect;
	}

	public CurrencyList() {
		this.currencies = new ArrayList<Currency>();
	}

	public void addCurrency(Currency Currency) {
		currencies.add(Currency);
	}

	public List<Currency> getCurrencys() {
		return currencies;
	}

	public void setCurrencys(List<Currency> brands) {
		this.currencies = brands;
	}
}
