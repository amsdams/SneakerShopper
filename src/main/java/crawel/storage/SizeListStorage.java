package crawel.storage;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.Size;
import crawel.pojo.SizeList;

public class SizeListStorage {
	private static final String ALL_SIZES_JSON = "allSizes.json";

	private static final Logger LOGGER = LoggerFactory.getLogger(SizeListStorage.class);

	private static final SizeListStorage instance = new SizeListStorage();

	private SizeListStorage() {
	}

	public static SizeListStorage getInstance() {
		return instance;
	}

	public static SizeList get() {
		return get(ALL_SIZES_JSON);

	}

	public static SizeList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		SizeList allSizes = new SizeList();
		try {
			allSizes = mapper.readValue(new File(fileName), SizeList.class);
		} catch (IOException e) {
			LOGGER.error("could not open file, creating one", e);
			SizeList sizeList = new SizeList();
			for (double d = 3.0; d < 15.0; d = d + 0.5) {
				Size size = new Size(d, "US");

				sizeList.addSize(size);

			}

			put(sizeList);
		}

		return allSizes;

	}

	public static void print(SizeList sizeList) {
		for (Size size : sizeList.getSizes()) {
			LOGGER.info(size.toString());
		}
		LOGGER.info("printed " + sizeList.getSizes().size());

	}

	public static void put(SizeList sizeList) {

		put(sizeList, ALL_SIZES_JSON);

	}

	public static void put(SizeList sizeList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), sizeList);

		} catch (JsonGenerationException e) {
			LOGGER.error("could not generate json", e);
		} catch (JsonMappingException e) {
			LOGGER.error("could not map json", e);
		} catch (IOException e) {
			LOGGER.error("could not write file", e);
		}

	}
}
