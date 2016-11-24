package crawel.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Currency implements Comparable<Currency> {
	private String name;

	private String symbol;


	

	

	@Override
	public int compareTo(Currency o) {

		return this.getName().compareTo(o.getName());
	}

}
