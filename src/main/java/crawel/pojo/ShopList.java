package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class ShopList {

	private List<Shop> shops;

	public ShopList() {
		this.shops = new ArrayList<>();
	}

	

	public void addShop(Shop shop) {
		shops.add(shop);
	}

}
