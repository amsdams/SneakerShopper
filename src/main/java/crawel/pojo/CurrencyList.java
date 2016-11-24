package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CurrencyList {

	private List<Currency> currencies;

	public CurrencyList() {
		this.currencies = new ArrayList<>();
	}

	

	public void addCurrency(Currency currency) {
		currencies.add(currency);
	}

}
