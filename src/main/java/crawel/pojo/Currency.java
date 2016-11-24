package crawel.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Currency implements Comparable<Currency> {
	private String name;

	private String symbol;

	public Currency() {
		
	}

	

	

	@Override
	public int compareTo(Currency o) {

		return this.getName().compareTo(o.getName());
	}

}
