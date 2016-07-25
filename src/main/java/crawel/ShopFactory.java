package crawel;

import java.util.List;

public class ShopFactory {
	public static enum SHOPTYPES {
		OVERKILLSHOP, TITOLOSHOP, SNEAKAVENUE
	}

	public Shop getShop(SHOPTYPES shopType){
		switch (shopType){
		case OVERKILLSHOP:
			return new OverkillShop();
			
		
		default:
			return null;
				
		}
	}

}
