package org.team3309.vision;

public class Utils {

	public static double pxToFeet(double targetPx, double px){
		return (Constants.TARGET_WIDTH_FT*px)/targetPx;
	}
	
	public static double distance(double targetPx){
		return Constants.CAM_WIDTH/(Math.tan(Math.toRadians(37.5))*targetPx);
	}
}
