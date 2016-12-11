package crawel;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import crawel.crawler.PageCrawlerClient;
import crawel.crawler.PageCrawlerOverview;
import crawel.pojo.Page;
import crawel.pojo.PageList;
import crawel.pojo.Shop;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ShopRunner implements Runnable {

	private final Shop shop;

	@Override
	public void run() {
		
		try {
			if (shop.getRunnable().equals(Boolean.FALSE)) {
				log.info("will not run shop {}", shop.getBaseUrl());
			} else {
				PageCrawlerClient client = new PageCrawlerClient();
				String url=shop.getBaseUrl();
				
				HtmlPage page = client.getHtmlPage(url, shop);
				
				PageCrawlerOverview web = new PageCrawlerOverview();

				web.addProductsToList(client, page, shop);

			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
	}
}
