package crawel;

import crawel.shops.OverkillShop;
import crawel.shops.SneakAVenue;
import crawel.shops.SneakerBaas;
import crawel.shops.TitoloShop;

public class ShopFactory {
	public static enum SHOPTYPES {
		OVERKILLSHOP, TITOLOSHOP, SNEAKAVENUE, SNEAKERBAAS
	}

	public Shop getShop(SHOPTYPES shopType) {
		switch (shopType) {
		case OVERKILLSHOP:
			return new OverkillShop();
		case TITOLOSHOP:
			return new TitoloShop();
		case SNEAKAVENUE:
			return new SneakAVenue();
		case SNEAKERBAAS:
			return new SneakerBaas();
		default:
			return null;

		}
	}

}
