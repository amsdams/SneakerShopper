import org.junit.Test;

import crawel.helpers.BrandHelper;
import crawel.pojo.Brand;
import crawel.pojo.BrandList;
import crawel.pojo.Product;
import crawel.pojo.ProductList;
import crawel.storage.BrandListStorage;

public class BrandHelperTest {

	@Test
	public void saveTest() {
		BrandList brandList = BrandListStorage.get();
		Brand x = new Brand("Test x Brand");
		brandList.addBrand(x);

		Brand xX = new Brand("BRAND x TEST");
		brandList.addBrand(xX);

		BrandListStorage.put(brandList, "testBrands.json");
	}

	@Test
	public void matchTest() {

		BrandList brandList = new BrandList();
		//brandList.addBrand(new Brand("PUMA"));
		brandList.addBrand(new Brand("PUMA X ALIFE"));
		brandList.addBrand(new Brand("PUMA X BAPE"));
		
		ProductList productList = new ProductList();
		//productList.addProduct(new Product("PUMA"));
		//productList.addProduct(new Product("PUMA"));
		productList.addProduct(new Product("PUMA X ALIFE"));
		productList.addProduct(new Product("ALIFE X PUMA"));

		//productList.addProduct(new Product("BOG"));

		//productList.addProduct(new Product("PUMA X ALIFE BOG"));
		//productList.addProduct(new Product("ALIFE X PUMA BOG"));
		//productList.addProduct(new Product("BOG PUMA X ALIFE "));
		//productList.addProduct(new Product("BOG ALIFE X PUMA "));

		for (Product product : productList.getProducts()) {
			String name = product.getName();
			String brand = BrandHelper.getBrandName(name, brandList);

			String newName = BrandHelper.removeBrandName(name, brandList);
			product.setBrandNameRemovedFromName(true);
			System.out.println("orginal name " + name + "  , new name " + newName + "  , brand name " + brand);
		}

	}

	
}
