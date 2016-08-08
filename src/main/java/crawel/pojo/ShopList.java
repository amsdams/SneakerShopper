package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

public class ShopList {

	private List<Shop> shops = new ArrayList<Shop>();

	public ShopList() {

	}

	public ShopList(List<Shop> collect) {
		this.shops = collect;
	}

	public void addShop(Shop shop) {
		shops.add(shop);
	}

	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}
}
