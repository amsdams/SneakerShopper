package crawel.pojo;

import java.util.Comparator;

import lombok.Data;

@Data
public class Size implements Comparable<Size> {
	public static Comparator<Size> SIZECOMPARATOR = new Comparator<Size>() {

		@Override
		public int compare(Size size1, Size size2) {
			// TODO no idea yet
			Integer lenght1 = size1.getSize().length();
			Integer lenght2 = size2.getSize().length();

			return lenght2.compareTo(lenght1);

		}

	};
	private String metric;
	private String size;

	public Size() {

	}

	public Size(String size, String metric) {
		this.metric = metric;
		this.size = size;
	}

	@Override
	public int compareTo(Size o) {
		return this.getSize().compareTo(o.getSize());
	}

}
