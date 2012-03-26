package org.team3309.vision;

public class Utils {

	public static double pxToFeet(double targetPx, double px){
		return (Constants.TARGET_WIDTH_FT*px)/targetPx;
	}

	public static double distance(double targetPx){
		return Constants.CAM_WIDTH/(Math.tan(Math.toRadians(37.5))*targetPx);
	}

	public static double kinectDistance(int rawDepth){
		if (rawDepth < 2047)
		{
			return 1.0 / (rawDepth * -0.0030711016 + 3.3309495161);
		}
		return 0;
	}
}
