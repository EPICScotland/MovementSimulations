package animalMovementSimulation;

import math.Distributions;

public class LocationCoordinate {

	static 			String   delim 		= RandomMovementSimulator.delim;	// ","
	static 			CoordType coordType = CoordType.EASTING_NORTHING;
	
	public static String getCoordinateNames() {
		
		if (coordType.equals(CoordType.EASTING_NORTHING)) {
			String txt = "Easting" + delim + "Northing";
			return txt;
		} else if (coordType.equals(CoordType.LAT_LON)) {
			String txt = "Lat" + delim + "Lon";
			return txt;
		} else {
			return null;
		}
		
	}
	
	static double[] getRandomCoordinate() {
		double[] coord 	= new double[2];
		if (coordType.equals(CoordType.EASTING_NORTHING)) {
			coord[0]		= Distributions.randomInt(10000000);
			coord[1]		= Distributions.randomInt(10000000);
		} else {
			coord[0]	= (Distributions.randomUniform()*150.0)-75.0;
			coord[0]	= (Distributions.randomUniform()*180.0)-90.0;
		}
		return coord;
	}
	
	/////////////////////////////////////////////////////////////////
	// instance variables
	
	double[] coords = {0.0, 0.0};
	
	public LocationCoordinate() {
		coords = getRandomCoordinate();
	}
	
	//////////////////////////////////////////////////////////////////
	
	public void setCoords(double[] coords) {
		this.coords = coords;
	}
	
	public double[] getCoords() {
		return coords;
	}
	
	//////////////////////////////////////////////////////////////////
	
	private String intString() {
		
		String txt = "" + String.format("%07d", (int)coords[0]);
		
		//String txt = "" + (int)coords[0];
		for (int i = 1; i < coords.length; i++) {
			//txt = txt + delim + (int)coords[i];
			txt = txt + delim + String.format("%07d", (int)coords[i]);
		}
		return txt;
	}
	
	private String doubleString() {
		String txt = "" + coords[0];
		for (int i = 1; i < coords.length; i++) {
			txt = txt + delim + coords[i];
		}
		return txt;
	}
	
	public String toString() {
		if (coordType.equals(CoordType.EASTING_NORTHING)) {
			return intString();
		} else if (coordType.equals(CoordType.LAT_LON)){
			return doubleString();
		} else {
			return doubleString();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// Helper class
	
	public enum CoordType {
		EASTING_NORTHING, LAT_LON;
	}
	
}
