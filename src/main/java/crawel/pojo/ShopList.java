package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import crawel.crawler.PageCrawlerOverview;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class ShopList {

	private List<Shop> shops;
	private Boolean limitEnabled;
	private Boolean detailsEnabled;
	private Boolean javaScriptEnabled;

	public ShopList() {
		this.shops = new ArrayList<>();
	}

	public void addShop(Shop shop) {
		shops.add(shop);
	}

}
