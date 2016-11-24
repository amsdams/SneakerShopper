package crawel.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class SizeKids extends Size {

	private String inEurope;

	private double inCentiMeters;

	private String inUnitedStates;

	private String inUnitedKingdom;

	

}
