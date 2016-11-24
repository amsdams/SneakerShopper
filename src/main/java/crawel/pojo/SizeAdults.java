package crawel.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class SizeAdults extends Size {

	private String inEurope;
	private double inCentiMeters;
	private String inUnitedKingdom;
	private String inUnitedStatesMen;
	private String inUnitedStatesWomen;

	

}
