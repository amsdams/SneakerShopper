package crawel;

import java.util.List;

import crawel.shops.OverkillShop;
import crawel.shops.SneakAVenue;
import crawel.shops.TitoloShop;

public class ShopFactory {
	public static enum SHOPTYPES {
		OVERKILLSHOP, TITOLOSHOP, SNEAKAVENUE
	}

	public Shop getShop(SHOPTYPES shopType) {
		switch (shopType) {
		case OVERKILLSHOP:
			return new OverkillShop();
		case TITOLOSHOP:
			return new TitoloShop();
		case SNEAKAVENUE:
			return new SneakAVenue();

		default:
			return null;

		}
	}

}
