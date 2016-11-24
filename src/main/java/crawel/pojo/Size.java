package crawel.pojo;

import java.util.Comparator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
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

	

	

	@Override
	public int compareTo(Size o) {
		return this.getSize().compareTo(o.getSize());
	}

}
