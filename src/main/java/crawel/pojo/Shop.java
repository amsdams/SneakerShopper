package crawel.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Shop {
	public String productOldPriceSelector;
	public String productUrlSelector;
	public String productNameSelector;
	public String productBrandNameSelector;
	public String productNewPriceSelector;
	public String productCurrencySelector;
	public String nextPageSelector;
	public String baseUrl;
	public ProductList productList;
	public PageList pageList;

	public BrandList brandList;
	public CurrencyList currencyList;
	public Boolean runnable;
	public Boolean javaScriptEnabled;
	public Integer timeout;
	public String productsSelector;
	public Boolean limitEnabled;
	public Boolean detailsEnabled;
	public String referrer;
	public String productDetailsSizeSelector;
	public String productDetailsSizeType;
	public String productDetailsSizesSelector;

	public Shop(String referrer) {
		this.referrer = referrer;
	}
}