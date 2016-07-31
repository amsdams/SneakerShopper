package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrencyList {
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyList.class);

	private List<Currency> brands = new ArrayList<Currency>();

	public void addCurrency(Currency Currency) {
		brands.add(Currency);
	}

	public List<Currency> getCurrencys() {
		return brands;
	}

	public void setCurrencys(List<Currency> brands) {
		this.brands = brands;
	}
}
