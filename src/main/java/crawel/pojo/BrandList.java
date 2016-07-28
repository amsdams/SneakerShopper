package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

public class BrandList {
	private List<Brand> brands = new ArrayList<Brand>();

	public void addBrand(Brand Brand) {
		brands.add(Brand);
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}
}
