package crawel.helpers;

import java.util.ArrayList;
import java.util.List;

public class BrandCombinations {

	public String[] brandArray;
	public String splitter;

	public BrandCombinations(String words, String splitter) {
		this.brandCombinations = new ArrayList<String>();

		this.splitter = splitter;
		brandArray = words.split(splitter);
		doAnagram(brandArray.length);
	}

	public void changeOrder(int newsize) {
		int j;
		int pointAt = brandArray.length - newsize;
		String temp = brandArray[pointAt];

		for (j = pointAt + 1; j < brandArray.length; j++) {
			brandArray[j - 1] = brandArray[j];
		}

		brandArray[j - 1] = temp;

	}

	public void doAnagram(int newsize) {
		if (newsize == 1) {
			return;
		}
		for (int i = 0; i < newsize; i++) {
			doAnagram(newsize - 1);
			if (newsize == 2) {
				add();
			}
			changeOrder(newsize);
		}
	}

	private List<String> brandCombinations;

	public void add() {
		StringBuilder brandBuilder = new StringBuilder();
		for (int i = 0; i < brandArray.length; i++) {
			if (i == brandArray.length - 1) {
				brandBuilder.append(brandArray[i]);
			} else {
				brandBuilder.append(brandArray[i] + splitter);
			}
		}
		brandCombinations.add(brandBuilder.toString());
	}

	public static List<String> getBrandCombos(String brandName, String splitter) {

		BrandCombinations test1 = new BrandCombinations(brandName, splitter);

		return test1.brandCombinations;

	}

}