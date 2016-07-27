package crawel;

import crawel.shops.AFew;
import crawel.shops.AsphaltGold;
import crawel.shops.EndClothing;
import crawel.shops.Offspring;
import crawel.shops.OverkillShop;
import crawel.shops.SlamJamSocialism;
import crawel.shops.SneakAVenue;
import crawel.shops.SneakerBaas;
import crawel.shops.TitoloShop;
import crawel.shops._43Einhalb;
import crawel.shops._5Pointz;

public class ShopFactory {
	public static enum SHOPTYPES {
		OVERKILLSHOP, TITOLOSHOP, SNEAKAVENUE, _43EINHALB, _5POINTZ, ENDCLOTHING, SNEAKERBAAS, OFFSPRING, SLAMJAMSOCIALISM, ASPHALTGOLD, AFEW
	}

	public Shop getShop(SHOPTYPES shopType) {
		switch (shopType) {
		case OVERKILLSHOP:
			return new OverkillShop();
		case TITOLOSHOP:
			return new TitoloShop();
		case SNEAKAVENUE:
			return new SneakAVenue();
		case _43EINHALB:
			return new _43Einhalb();
		case _5POINTZ:
			return new _5Pointz();
		case ENDCLOTHING:
			return new EndClothing();
		case SNEAKERBAAS:
			return new SneakerBaas();
		case OFFSPRING:
			return new Offspring();
		case SLAMJAMSOCIALISM:
			return new SlamJamSocialism();
		case ASPHALTGOLD:
			return new AsphaltGold();
		case AFEW:
			return new AFew();
		default:
			return null;

		}
	}

}
