package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class BrandList {

	private List<Brand> brands;

	public BrandList() {
		brands = new ArrayList<>();
	}



	public void addBrand(Brand brand) {
		brands.add(brand);
	}

}
