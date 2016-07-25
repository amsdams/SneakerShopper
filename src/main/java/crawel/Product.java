package crawel;

public class Product {
	private Double newPrice;

	private Double oldPrice;

	private String brandName;

	private String name;

	private String url;

	private String shopName;

	private String currency;
	
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBrandName() {
		return brandName;
	}

	public String getName() {
		return name;
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
