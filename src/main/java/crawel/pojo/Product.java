package crawel.pojo;

import java.math.BigDecimal;
import java.util.Comparator;

import lombok.Data;

@Data
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

			BigDecimal discount1 = product1.getDiscountInEU();
			BigDecimal discount2 = product2.getDiscountInEU();

			return discount2.compareTo(discount1);

		}

	};

	public static Comparator<Product> NewPriceComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			BigDecimal productName1 = product1.getNewPriceInEuro();
			BigDecimal productName2 = product2.getNewPriceInEuro();

			return productName1.compareTo(productName2);

		}

	};

	public static Comparator<Product> OldPriceComparator = new Comparator<Product>() {

		@Override
		public int compare(Product product1, Product product2) {

			BigDecimal productName1 = product1.getOldPriceInEuro();
			BigDecimal productName2 = product2.getOldPriceInEuro();

			return productName1.compareTo(productName2);

		}

	};

	private BigDecimal newPriceRaw;

	private BigDecimal oldPriceRaw;

	private BigDecimal newPriceInEuro;

	private BigDecimal oldPriceInEuro;

	private String brandName;

	private String name;

	private String url;

	private String shopName;

	private BigDecimal discountInEU;

	private SizeList sizesRaw;
	private SizeList sizesInEU;

	private Currency currencyRaw;
	private boolean brandNameRemovedFromName;

	public Product() {

	}

	

	@Override
	public int compareTo(Product o) {

		return this.getBrandName().compareTo(getBrandName());

	}

	// TODO check output;
	public BigDecimal getDiscountInEU() {
		BigDecimal temp = BigDecimal.ONE.multiply(this.getNewPriceInEuro().divide(this.getNewPriceInEuro()));
		BigDecimal multiplyer = new BigDecimal(100);

		this.discountInEU = temp.multiply(multiplyer);
		return this.discountInEU;
	}

}
