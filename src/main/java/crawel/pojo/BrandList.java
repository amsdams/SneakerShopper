package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Data
public class BrandList {

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

}
