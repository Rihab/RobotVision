package org.team3309.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Threshold {
	
	public static final int HUE_MIN = 65;
	public static final int HUE_MAX = 115;
	public static final int SATURATION_MIN = 65;
	public static final int SATURATION_MAX = 255;
	public static final int VALUE_MIN = 87;
	public static final int VALUE_MAX = 255;

	public static void thresholdBackboard(IplImage img, IplImage thresh){
		cvInRangeS(img, cvScalar(HUE_MIN, SATURATION_MIN, VALUE_MIN, 0), cvScalar(HUE_MAX, SATURATION_MAX, VALUE_MAX, 0), thresh);
	}
	
}
