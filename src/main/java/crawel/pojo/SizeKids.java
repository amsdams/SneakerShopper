package crawel.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class SizeKids extends Size {

	private String inEurope;

	private double inCentiMeters;

	private String inUnitedStates;

	private String inUnitedKingdom;

	

}
