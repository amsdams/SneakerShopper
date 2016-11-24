package crawel.pojo;

import lombok.Data;

@Data
public class Currency implements Comparable<Currency> {
	private String name;

	private String symbol;

	public Currency() {
		this.name = "";
		this.symbol = "";
	}

	public Currency(String name) {
		this.name = name;
		this.symbol = "";
	}

	public Currency(String name, String symbol) {
		this.name = name;
		this.symbol = symbol;
	}

	@Override
	public int compareTo(Currency o) {

		return this.getName().compareTo(o.getName());
	}

}
