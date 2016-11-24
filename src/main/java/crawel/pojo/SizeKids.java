package crawel.pojo;

import lombok.Data;

@Data
public class SizeKids extends Size {

	private String inEurope;

	private double inCentiMeters;

	private String inUnitedStates;

	private String inUnitedKingdom;

	public SizeKids(String inEurope, double inCentiMeters, String inUnitedKingdom, String inUnitedStates) {

		this.inEurope = inEurope;
		this.inCentiMeters = inCentiMeters;
		this.inUnitedKingdom = inUnitedKingdom;
		this.inUnitedStates = inUnitedStates;
	}

}
