package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PageList {
	private static PageList instance = null;

	protected PageList() {
		// Exists only to defeat instantiation.
		this.pages = new ArrayList<>();
	}

	public static PageList getInstance() {
		if (instance == null) {
			instance = new PageList();
		}
		return instance;
	}

	private List<Page> pages;

	/*
	 * public PageList() {
	 * 
	 * }
	 */

	public void addPage(Page page) throws Exception {
		if (pages.contains(page)) {
			throw new Exception("already added page "+page.getUrl());
			
		}
		pages.add(page);
	}
}
