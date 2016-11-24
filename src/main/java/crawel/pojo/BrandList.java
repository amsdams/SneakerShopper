package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandList {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandList.class);

	private List<Brand> brands;

	public BrandList() {
		brands = new ArrayList<>();
	}

	public BrandList(List<Brand> collect) {
		this.brands = collect;
	}

	public void addBrand(Brand brand) {
		brands.add(brand);
	}

	public List<Brand> getBrands() {

		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Brand s: this.brands){
			sb.append(s.toString());
		}
		return "BrandList [brands=" + sb.toString() + "]";
	}
}
