package crawel;

import java.util.Comparator;

public class Product implements Comparable<Product> {

	public static Comparator<Product> BrandNameComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			String productName1 = product1.getBrandName().toUpperCase();
			String productName2 = product2.getBrandName().toUpperCase();

			// ascending order
			return productName1.compareTo(productName2);

			// descending order
			// return productName2.compareTo(productName1);
		}

	};

	public static Comparator<Product> NameComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			String productName1 = product1.getName().toUpperCase();
			String productName2 = product2.getName().toUpperCase();

			// ascending order
			return productName1.compareTo(productName2);

			// descending order
			// return productName2.compareTo(productName1);
		}

	};

	public static Comparator<Product> NewPriceComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			Double productName1 = product1.getNewPrice();
			Double productName2 = product2.getNewPrice();

			// ascending order
			return productName1.compareTo(productName2);

			// descending order
			// return productName2.compareTo(productName1);
		}

	};

	public static Comparator<Product> OldPriceComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			Double productName1 = product1.getOldPrice();
			Double productName2 = product2.getOldPrice();

			// ascending order
			return productName1.compareTo(productName2);

			// descending order
			// return productName2.compareTo(productName1);
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

		// ascending order
		return this.getBrandName().compareTo(otherBrandName);

		// descending order
		// return compareQuantity - this.quantity;
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
		return newPrice;
	}

	public Double getOldPrice() {
		return oldPrice;
	}

	public String getShopName() {
		return shopName;
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
		return "Product [newPrice=" + newPrice + ", oldPrice=" + oldPrice + ", brandName=" + brandName + ", name="
				+ name + ", url=" + url + ", currency=" + currency + ", shopName=" + shopName + "]";
	}

}
