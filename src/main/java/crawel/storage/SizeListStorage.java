package crawel.storage;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.Constants;
import crawel.pojo.Size;
import crawel.pojo.SizeList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SizeListStorage {
	private static final String ALL_SIZES_JSON = "allSizes.json";

	private static final SizeListStorage instance = new SizeListStorage();

	public static SizeList get() {
		return get(ALL_SIZES_JSON);

	}

	public static SizeList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		SizeList allSizes = new SizeList();
		try {
			allSizes = mapper.readValue(new File(fileName), SizeList.class);
		} catch (Exception e) {
			log.error(Constants.CAUGHT_EXCEPTION_CREATING_NEW, e.getMessage(), e);

			SizeList sizeList = new SizeList();
			for (Double d = 3.0; d < 15.0; d = d + 0.5) {
				Size size = new Size(d.toString(), "US");

				sizeList.addSize(size);

			}

			put(sizeList);
		}

		return allSizes;

	}

	public static SizeListStorage getInstance() {
		return instance;
	}

	public static void print(SizeList sizeList) {
		for (Size size : sizeList.getSizes()) {
			log.info(size.toString());
		}
		log.info("printed " + sizeList.getSizes().size());

	}

	public static void put(SizeList sizeList) {

		put(sizeList, ALL_SIZES_JSON);

	}

	public static void put(SizeList sizeList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), sizeList);

		} catch (Exception e) {
			log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
		}

	}

	private SizeListStorage() {
	}
}
