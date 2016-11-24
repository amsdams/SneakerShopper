package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
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

}
