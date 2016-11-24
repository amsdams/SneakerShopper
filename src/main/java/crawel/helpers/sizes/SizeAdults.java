package crawel.helpers.sizes;

import crawel.pojo.Size;

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

	public double getInCentiMeters() {
		return inCentiMeters;
	}

	public String getInEurope() {
		return inEurope;
	}

	public String getInUnitedKingdom() {
		return inUnitedKingdom;
	}

	public String getInUnitedStatesMen() {
		return inUnitedStatesMen;
	}

	public String getInUnitedStatesWomen() {
		return inUnitedStatesWomen;
	}

	public void setInCentiMeters(double inCentiMeters) {
		this.inCentiMeters = inCentiMeters;
	}

	public void setInEurope(String inEurope) {
		this.inEurope = inEurope;
	}

	public void setInUnitedKingdom(String inUnitedKingdom) {
		this.inUnitedKingdom = inUnitedKingdom;
	}

	public void setInUnitedStatesMen(String inUnitedStatesMen) {
		this.inUnitedStatesMen = inUnitedStatesMen;
	}

	public void setInUnitedStatesWomen(String inUnitedStatesWomen) {
		this.inUnitedStatesWomen = inUnitedStatesWomen;
	}

}
