package crawel.pojo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

import crawel.Crawel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import sun.util.logging.resources.logging;

@Slf4j
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
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

	@Override
	public int compareTo(Product o) {

		return this.getBrandName().compareTo(getBrandName());

	}

	// TODO check output;
	public BigDecimal getDiscountInEU() {
		try {
			if (this.getNewPriceInEuro() != null && this.getOldPriceInEuro() != null) {
				BigDecimal temp = this.getNewPriceInEuro().divide(this.getOldPriceInEuro(), RoundingMode.HALF_UP);
				BigDecimal multiplyer = new BigDecimal(100);

				this.discountInEU = temp.multiply(multiplyer);
			}
		} catch (Exception e) {
			log.error("Caught exception {}", e.getMessage(), e);
		}
		return this.discountInEU;
	}

	public Product(String name) {
		this.name = name;
	}

}
