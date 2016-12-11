package crawel.helpers;

import crawel.Constants;
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
			if (log.isErrorEnabled()){
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return returnText;
	}
}
