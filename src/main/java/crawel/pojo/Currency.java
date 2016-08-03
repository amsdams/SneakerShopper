package crawel.pojo;

public class Currency {
	private String name;

	private String symbol;

	public Currency() {

	}

	public Currency(String name) {
		this.name = name;
	}

	public Currency(String name, String symbol) {
		this.name = name;
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "Currency [name=" + name + ", symbol=" + symbol + "]";
	}
}
