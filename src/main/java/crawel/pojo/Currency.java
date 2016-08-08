package crawel.pojo;

public class Currency implements Comparable<Currency> {
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
		return name!=null?name.toUpperCase():"";
	}

	public String getSymbol() {
		return symbol!=null?symbol.toUpperCase():"";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "Currency [name=" + this.getName() + ", symbol=" + this.getSymbol() + "]";
	}

	@Override
	public int compareTo(Currency o) {

		return this.getName().compareTo(o.getName());
	}
}
