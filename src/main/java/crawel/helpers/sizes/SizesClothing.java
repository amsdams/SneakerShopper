package crawel.helpers.sizes;

import java.util.ArrayList;
import java.util.List;

import crawel.pojo.SizeClothing;

public class SizesClothing {

	public static List<SizeClothing> getSizesClothing() {
		List<SizeClothing> sizes = new ArrayList<>();

		sizes.add(new SizeClothing("XXS"));
		sizes.add(new SizeClothing("XS"));
		sizes.add(new SizeClothing("S"));

		sizes.add(new SizeClothing("M"));

		sizes.add(new SizeClothing("L"));
		sizes.add(new SizeClothing("XL"));
		sizes.add(new SizeClothing("XXL"));
		sizes.add(new SizeClothing("2XL"));
		sizes.add(new SizeClothing("3XL"));

		return sizes;
	}

}
