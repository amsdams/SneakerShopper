package crawel.pojo;

import lombok.Data;

@Data
public class SizeClothing extends Size {

	private String inLabel;

	public SizeClothing(String inLabel) {

		this.inLabel = inLabel;

	}

}
