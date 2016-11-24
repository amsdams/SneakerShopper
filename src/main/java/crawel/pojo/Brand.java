package crawel.pojo;

import java.util.Comparator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
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



	

	@Override
	public int compareTo(Brand o) {
		return this.getName().compareTo(o.getName());
	}

}
