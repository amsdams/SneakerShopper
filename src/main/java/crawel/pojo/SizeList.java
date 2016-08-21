package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SizeList {
	private static final Logger LOGGER = LoggerFactory.getLogger(SizeList.class);

	private List<Size> sizes;

	public SizeList() {
		sizes = new ArrayList<>();
	}

	public SizeList(List<Size> collect) {
		this.sizes = collect;
	}

	public void addSize(Size size) {
		sizes.add(size);
	}

	public List<Size> getSizes() {

		return sizes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Size s: this.sizes){
			sb.append(s.toString());
		}
		return "SizeList [sizes=" + sb.toString() + "]";
	}

	public void setSizes(List<Size> sizes) {
		this.sizes = sizes;
	}
}
