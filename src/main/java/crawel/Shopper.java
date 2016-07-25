package crawel;

public class Shopper implements Runnable {
	private final Shop shop;

	Shopper(Shop shop) {
		this.shop = shop;
	}
	
	public void run() {

		ProductList products = shop.getProductList();
	
		System.out.println("found "+products.getProducts().size());
	}
}
