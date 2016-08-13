package crawel.pojo;

import java.util.Comparator;

public class Product implements Comparable<Product> {

	public static Comparator<Product> BrandNameComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			String productName1 = product1.getBrandName();
			String productName2 = product2.getBrandName();

			return productName1.compareTo(productName2);

		}

	};

	public static Comparator<Product> NameComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			String productName1 = product1.getName();
			String productName2 = product2.getName();

			return productName1.compareTo(productName2);

		}

	};

	public static Comparator<Product> ShopNameComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			String productName1 = product1.getShopName();
			String productName2 = product2.getShopName();

			return productName1.compareTo(productName2);

		}

	};

	public static Comparator<Product> DiscountComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			Double discount1 = product1.getDiscount();
			Double discount2 = product2.getDiscount();

			return discount2.compareTo(discount1);
			// return Double.compare(discount1, discount2);

		}

	};

	public static Comparator<Product> NewPriceComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			Double productName1 = product1.getNewPrice();
			Double productName2 = product2.getNewPrice();

			return productName1.compareTo(productName2);

		}

	};

	public static Comparator<Product> OldPriceComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			Double productName1 = product1.getOldPrice();
			Double productName2 = product2.getOldPrice();

			return productName1.compareTo(productName2);

		}

	};

	private Double newPrice;

	private Double oldPrice;

	private Double newPriceInEuro;

	private Double oldPriceInEuro;

	private String brandName;

	private String name;

	private String url;

	private String shopName;

	private Double discount;

	private Currency currency;
	private boolean brandNameRemovedFromName;

	public Product() {

	}

	public Product(String productName) {
		this.name = productName;
	}

	@Override
	public int compareTo(Product o) {

		return this.getBrandName().compareTo(getBrandName());

	}

	public String getBrandName() {

		return brandName == null ? "" : brandName.toUpperCase();
	}

	public Currency getCurrency() {
		return currency == null ? new Currency() : currency;
	}

	public Double getDiscount() {
		// this.discount = ((this.getNewPrice()/this.getOldPrice())*100)-100;
		this.discount = (1 - (this.getNewPrice() / this.getOldPrice())) * 100;
		return this.discount;
	}

	public String getName() {
		return name == null ? "" : name.toUpperCase();
	}

	public Double getNewPrice() {
		return newPrice != null ? newPrice : 0.0;
	}

	public Double getNewPriceInEuro() {
		return newPriceInEuro;
	}

	public Double getOldPrice() {
		return oldPrice != null ? oldPrice : 0.0;
	}

	public Double getOldPriceInEuro() {
		return oldPriceInEuro;
	}

	public String getShopName() {
		return shopName.toUpperCase();
	}

	public String getUrl() {
		return url == null ? "" : url;
	}

	public boolean isBrandNameRemovedFromName() {
		return brandNameRemovedFromName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public void setBrandNameRemovedFromName(boolean brandNameRemovedFromName) {
		this.brandNameRemovedFromName = brandNameRemovedFromName;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
	}

	public void setNewPriceInEuro(Double newPriceInEuro) {
		this.newPriceInEuro = newPriceInEuro;
	}

	public void setOldPrice(Double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public void setOldPriceInEuro(Double oldPriceInEuro) {
		this.oldPriceInEuro = oldPriceInEuro;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;

	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Product [brandName=" + this.getBrandName() + ", name=" + this.getName() + ", newPriceInEuro="
				+ this.getNewPriceInEuro() + ", oldPriceInEuro=" + this.getOldPriceInEuro() + ", currency="
				+ this.getCurrency() + ",  url=" + this.getUrl() + ",  shopName=" + this.getShopName() + ",  discount="
				+ this.getDiscount() + ", newPrice=" + this.getNewPrice() + ", oldPrice=" + this.getOldPrice() + "]";
	}

}
