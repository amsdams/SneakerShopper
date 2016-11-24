package crawel.pojo;

import lombok.Data;

@Data
public class SizeAdults extends Size {

	private String inEurope;
	private double inCentiMeters;
	private String inUnitedKingdom;
	private String inUnitedStatesMen;
	private String inUnitedStatesWomen;

	public SizeAdults(String inEurope, double inCentiMeters, String inUnitedKingdom, String inUnitedStatesMen,
			String inUnitedStatesWomen) {

		this.inEurope = inEurope;
		this.inCentiMeters = inCentiMeters;
		this.inUnitedKingdom = inUnitedKingdom;
		this.inUnitedStatesMen = inUnitedStatesMen;
		this.inUnitedStatesWomen = inUnitedStatesWomen;
	}

}
