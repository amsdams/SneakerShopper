package crawel.helpers.sizes;

import crawel.pojo.Size;

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

	public double getInCentiMeters() {
		return inCentiMeters;
	}

	public String getInEurope() {
		return inEurope;
	}

	public String getInUnitedKingdom() {
		return inUnitedKingdom;
	}

	public String getInUnitedStates() {
		return inUnitedStates;
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
	public void setInUnitedStates(String uS) {
		inUnitedStates = uS;
	}

}
