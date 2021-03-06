package crawel.helpers;

import java.util.ArrayList;
import java.util.List;

import crawel.Constants;
import crawel.helpers.sizes.SizesAdults;

import crawel.pojo.Size;
import crawel.pojo.SizeAdults;
import crawel.pojo.SizeList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SizeHelper {

	public static Size getSize(String text, SizeList sizeList) {
		List<Size> sizes = new ArrayList<>(sizeList.getSizes());
		sizes.sort(Size.SIZECOMPARATOR);

		String returnSize = Constants.UNKOWN_SIZE_SIZE;
		String returnMetric = Constants.UNKOWN_SIZE_METRIC;
		
		for (Size size : sizes) {

			String sizeName = size.getSize();
			if (sizeName.matches(text)) {
				returnSize = sizeName;
			}
		}
		if (Constants.UNKOWN_SIZE_SIZE.equals(returnSize)) {
			log.info(Constants.NOT_FOUND_SIZE_FROM, text);
		}
		return new Size(returnSize, returnMetric);
	}

	public static Size getSize(String text, String sizeType, SizeList sizeList) {
		List<Size> sizes = new ArrayList<>(sizeList.getSizes());
		sizes.sort(Size.SIZECOMPARATOR);

		String returnSize = "unknown";
		for (Size size : sizes) {

			String sizeName = size.getSize();
			if (sizeName.matches(text)) {
				returnSize = sizeName;
			}
		}
		if ("unknown".equals(returnSize)) {
			log.info("unable to match brand from {}", text);
		}
		return new Size(returnSize, sizeType);
	}

	private static String getSizeFrominUnitedKingdom(String size) {
		String sizeEU = "";
		for (SizeAdults sizeAdult : SizesAdults.getSizes()) {
			if (sizeAdult.getInUnitedKingdom().equals(size)) {
				sizeEU = sizeAdult.getInEurope();
			}
		}
		return sizeEU;
	}

	private static String getSizeFrominUnitedStates(String size) {
		String sizeEU = "";
		for (SizeAdults sizeAdult : SizesAdults.getSizes()) {
			if (sizeAdult.getInUnitedStatesMen().equals(size)) {
				sizeEU = sizeAdult.getInEurope();
			}
		}
		return sizeEU;
	}

	public static SizeList getSizesInEU(SizeList sizeList) {
		SizeList sizeListInEU = new SizeList();
		for (Size size : sizeList.getSizes()) {
			sizeListInEU.addSize(sizeToEU(size));
		}
		return sizeListInEU;
	}

	private static Size sizeToEU(Size size) {
		String sizeEU = size.getSize();
		if (sizeEU == null) {
			sizeEU = "unknown";
		}
		String region = size.getMetric();
		Size newSize = new Size();
		switch (region.toLowerCase()) {
		case "us":
			sizeEU = getSizeFrominUnitedStates(size.getSize());
			break;
		case "uk":
			sizeEU = getSizeFrominUnitedKingdom(size.getSize());
			break;
		default:
			sizeEU = size.getSize();
			break;

		}

	
		newSize.setSize(sizeEU);
		newSize.setMetric("eu");
		return newSize;
	}
}
