package crawel;

import java.util.Comparator;

public class Product implements Comparable<Product> {

	public static Comparator<Product> BrandNameComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			String productName1 = product1.getBrandName().toUpperCase();
			String productName2 = product2.getBrandName().toUpperCase();

			return productName1.compareTo(productName2);

		}

	};

	public static Comparator<Product> NameComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			String productName1 = product1.getName().toUpperCase();
			String productName2 = product2.getName().toUpperCase();

			return productName1.compareTo(productName2);

		}

	};

	public static Comparator<Product> ShopNameComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			String productName1 = product1.getShopName().toUpperCase();
			String productName2 = product2.getShopName().toUpperCase();

			return productName1.compareTo(productName2);

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

	private String brandName;

	private String name;

	private String url;

	private String shopName;

	private String currency;

	@Override
	public int compareTo(Product o) {
		String otherBrandName = o.getBrandName();

		return this.getBrandName().compareTo(otherBrandName);

	}

	public String getBrandName() {
		return brandName.toUpperCase();
	}

	public String getCurrency() {
		return currency;
	}

	public String getName() {
		return name.toUpperCase();
	}

	public Double getNewPrice() {
		return newPrice != null ? newPrice : 0.0;
	}

	public Double getOldPrice() {
		return oldPrice != null ? oldPrice : 0.0;
	}

	public String getShopName() {
		return shopName.toUpperCase();
	}

	public String getUrl() {
		return url;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNewPrice(Double newPrice) {
		this.newPrice = newPrice;
	}

	public void setOldPrice(Double oldPrice) {
		this.oldPrice = oldPrice;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;

	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Product [currency=" + currency + ",newPrice=" + newPrice + ", oldPrice=" + oldPrice + ", brandName="
				+ brandName + ", name=" + name + ", url=" + url + ",  shopName=" + shopName + "]";
	}

}
