package crawel.pojo;

import java.util.Comparator;

public class Brand implements Comparable<Brand> {
	public static Comparator<Brand> BRANDNAMESIZECOMPARATOR = new Comparator<Brand>() {

		@Override
		public int compare(Brand product1, Brand product2) {

			Integer lenght1 = product1.getName().length();
			Integer lenght2 = product2.getName().length();
			// long to sort
			return lenght2.compareTo(lenght1);

		}

	};
	private String name;

	public Brand() {
		this.name = "";
	}

	public Brand(String name) {
		this.name = name;

	}

	@Override
	public int compareTo(Brand o) {
		return this.getName().compareTo(o.getName());
	}

	public String getName() {
		return name.toUpperCase();
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Brand [name=" + name + "]";
	}
}
