package crawel;

import java.util.ArrayList;
import java.util.List;

public class ShopList {
	private List<Shop> shops = new ArrayList<Shop>();

	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

	public void addShop(Shop shop) {
		shops.add(shop);
	}
}
