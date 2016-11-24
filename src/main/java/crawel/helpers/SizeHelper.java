package crawel.helpers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawel.helpers.sizes.SizeAdults;
import crawel.helpers.sizes.SizesAdults;

import crawel.pojo.Size;
import crawel.pojo.SizeList;

public class SizeHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(SizeHelper.class);

	public static Size getSize(String text, SizeList sizeList) {
		List<Size> sizes = new ArrayList<>(sizeList.getSizes());
		sizes.sort(Size.SIZECOMPARATOR);

		String returnSize = "unknown";
		for (Size size : sizes) {

			String sizeName = size.getSizeRaw();
			if (sizeName.matches(text)) {
				returnSize = sizeName;
			}
		}
		if ("unknown".equals(returnSize)) {
			LOGGER.info("unable to match brand from {}", text);
		}
		return new Size(returnSize, "asd");
	}
	public static Size getSize(String text, String sizeType, SizeList sizeList) {
		List<Size> sizes = new ArrayList<>(sizeList.getSizes());
		sizes.sort(Size.SIZECOMPARATOR);

		String returnSize = "unknown";
		for (Size size : sizes) {

			String sizeName = size.getSizeRaw();
			if (sizeName.matches(text)) {
				returnSize = sizeName;
			}
		}
		if ("unknown".equals(returnSize)) {
			LOGGER.info("unable to match brand from {}", text);
		}
		return new Size(returnSize, sizeType);
	}
	
	public static SizeList getSizesInEU(SizeList sizeList) {
		SizeList sizeListInEU = new SizeList();
		for (Size size : sizeList.getSizes()) {
			sizeListInEU.addSize(sizeToEU(size));
		}
		return sizeListInEU;
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

	private static Size sizeToEU(Size size) {
		String sizeEU = size.getSizeRaw();
		if (sizeEU == null) {
			sizeEU = "unknown";
		}
		String region = size.getMetric();
		Size newSize = new Size();
		switch (region.toLowerCase()) {
		case "us":
			sizeEU = getSizeFrominUnitedStates(size.getSizeRaw());
			break;
		case "uk":
			sizeEU = getSizeFrominUnitedKingdom(size.getSizeRaw());
			break;
		default:
			sizeEU = size.getSizeRaw();
			break;

		}

		newSize.setSizeRaw(sizeEU);
		newSize.setMetric("eu");
		return newSize;
	}
}
