package crawel.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import crawel.Constants;
import crawel.pojo.Page;
import crawel.pojo.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data

public class PageCrawlerClient {
	

	public HtmlPage getHtmlPage(String url, Shop shop) {
		log.info("Fetching {}...", url);

		try {
			shop.getPageList().getInstance().addPage(new Page(url));
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
			return null;
		}
		
		HtmlPage page = null;
		try (final WebClient webClient = this.getWebClient(shop)) {
			
			page = webClient.getPage(url);

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(Constants.CAUGHT_EXCEPTION, e.getMessage(), e);
			}
		}
		return page;
	}

	public WebClient getWebClient(Shop shop) {
		WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);

		webClient.getOptions().setJavaScriptEnabled(shop.getJavaScriptEnabled());
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.waitForBackgroundJavaScript(shop.getTimeout());
		webClient.waitForBackgroundJavaScriptStartingBefore(shop.getTimeout());

		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setAppletEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setPopupBlockerEnabled(true);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setTimeout(shop.getTimeout());
		webClient.getOptions().setDoNotTrackEnabled(false);
		webClient.getOptions().setUseInsecureSSL(true);
		return webClient;
	}
}
