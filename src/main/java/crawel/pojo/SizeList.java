package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SizeList {

	private List<Size> sizes;

	public SizeList() {
		sizes = new ArrayList<>();
	}

	public SizeList(List<Size> sizes) {
		this.sizes = sizes;
	}

	public void addSize(Size size) {
		sizes.add(size);
	}

}
