package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SizeList {

	private List<Size> sizes;

	public SizeList() {
		sizes = new ArrayList<>();
	}

	

	public void addSize(Size size) {
		sizes.add(size);
	}

}
