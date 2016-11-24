package crawel.storage;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		} catch (IOException e) {
			log.error("could not open file, creating one", e);
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

		} catch (JsonGenerationException e) {
			log.error("could not generate json", e);
		} catch (JsonMappingException e) {
			log.error("could not map json", e);
		} catch (IOException e) {
			log.error("could not write file", e);
		}

	}

	private SizeListStorage() {
	}
}
