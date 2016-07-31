package crawel.pojo;

public class Currency {
	public Currency(String name, String symbol) {
		this.name = name;
		this.symbol = symbol;
	}

	public Currency() {

	}

	@Override
	public String toString() {
		return "Currency [name=" + name + ", symbol=" + symbol + "]";
	}

	public Currency(String name) {
		this.name = name;
	}

	private String name;
	private String symbol;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
