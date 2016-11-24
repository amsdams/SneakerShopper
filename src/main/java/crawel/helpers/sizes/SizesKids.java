package crawel.helpers.sizes;

import java.util.ArrayList;
import java.util.List;

import crawel.pojo.SizeKids;

public class SizesKids {

	public static List<SizeKids> getSizesKids() {
		List<SizeKids> sizes = new ArrayList<>();

		/*
		 * inEurope","1. LENGTE HIEL-TENEN","inUnitedKingdom","inUnitedStates
		 */

		sizes.add(new SizeKids("26.5", 15.7, "9", "9.5"));
		sizes.add(new SizeKids("27", 16.1, "9.5", "10"));
		sizes.add(new SizeKids("27.5", 16.6, "10", "10.5"));
		sizes.add(new SizeKids("28", 17, "10.5", "11"));
		sizes.add(new SizeKids("29", 17.4, "11", "11.5"));
		sizes.add(new SizeKids("30", 17.8, "11.5", "12"));
		sizes.add(new SizeKids("30.5", 18.3, "12", "12.5"));
		sizes.add(new SizeKids("31", 18.7, "12.5", "13"));
		sizes.add(new SizeKids("31.5", 19.1, "13", "13.5"));
		sizes.add(new SizeKids("32", 19.5, "13.5", "1"));
		sizes.add(new SizeKids("33", 20, "1", "1.5"));
		sizes.add(new SizeKids("33.5", 20.4, "1.5", "2vm"));
		sizes.add(new SizeKids("34", 20.8, "2", "2.5"));
		sizes.add(new SizeKids("35", 21.2, "2.5", "3"));
		sizes.add(new SizeKids("35.5", 21.6, "3", "3.5"));
		sizes.add(new SizeKids("36", 22.1, "3.5", "4"));
		sizes.add(new SizeKids("36 2/3", 22.5, "4", "4.5"));
		sizes.add(new SizeKids("37 1/3", 22.9, "4.5", "5"));
		sizes.add(new SizeKids("38", 23.3, "5", "5.5"));
		sizes.add(new SizeKids("38 2/3", 23.8, "5.5", "6"));
		sizes.add(new SizeKids("39 1/3", 24.2, "6", "6.5"));
		sizes.add(new SizeKids("40", 24.6, "6.5", "7"));

		return sizes;
	}

}
