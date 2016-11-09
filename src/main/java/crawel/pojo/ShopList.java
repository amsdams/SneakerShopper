package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

public class ShopList {

	private List<Shop> shops;

	public ShopList() {
		this.shops = new ArrayList<Shop>();
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
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Shop s: this.shops){
			sb.append(s.toString());
		}
		return "ShopList [shops=" + sb.toString() + "]";
	}
}
