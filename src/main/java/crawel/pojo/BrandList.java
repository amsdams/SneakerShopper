package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandList {
	private static final Logger LOGGER = LoggerFactory.getLogger(BrandList.class);

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
