package crawel.helpers;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextHelper {

	public static String sanitize(String text) {
		String returnText = "";
		try {
			returnText = text.replaceAll("\r", "");
			returnText = returnText.replaceAll("\t", "");
			returnText = returnText.replaceAll("\n", "");
			returnText = returnText.replaceAll("\"", "");
			returnText = returnText.replaceAll("\'", "");
			returnText = returnText.trim();
			returnText = returnText.toUpperCase();

		} catch (Exception e) {
			log.error("caught exception {}", e.getMessage(), e);
		}
		return returnText;
	}
}
