package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CurrencyList {

	private List<Currency> currencies;

	public CurrencyList() {
		this.currencies = new ArrayList<Currency>();
	}

	public CurrencyList(List<Currency> collect) {
		this.currencies = collect;
	}

	public void addCurrency(Currency Currency) {
		currencies.add(Currency);
	}

}
