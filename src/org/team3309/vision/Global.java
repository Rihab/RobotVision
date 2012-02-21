package org.team3309.vision;

import java.util.List;

import au.edu.jcu.v4l4j.Control;
import au.edu.jcu.v4l4j.ControlList;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.exceptions.ControlException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class Global {
	
	public static VideoDevice DEVICE;

	public static void init(){
		try {
			DEVICE = new VideoDevice(Constants.DEVICE);
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
		setSettings();
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DEVICE.releaseFrameGrabber();
		DEVICE.release();
	}
	
	private static void setSettings(){
		List<Control> list = DEVICE.getControlList().getList();
		for(Control c : list)
			System.out.println(c.getName()+" min:"+c.getMinValue()+" max:"+c.getMaxValue()+" default:"+c.getDefaultValue());
		ControlList controls = DEVICE.getControlList();
		try {
			controls.getControl("Contrast").setValue(Constants.CONTRAST);
			controls.getControl("Gamma").setValue(Constants.GAMMA);
			controls.getControl("Exposure, Auto").setValue(Constants.EXPOSURE_AUTO);
			controls.getControl("Gain").setValue(Constants.GAIN);
			controls.getControl("Saturation").setValue(Constants.SATURATION);
			controls.getControl("Sharpness").setValue(Constants.SHARPNESS);
			controls.getControl("Focus, Auto").setValue(Constants.FOCUS_AUTO);
			controls.getControl("Hue").setValue(Constants.HUE);
			controls.getControl("Exposure (Absolute)").setValue(Constants.EXPOSURE);
			controls.getControl("Brightness").setValue(Constants.BRIGHTNESS);
			controls.getControl("Focus (absolute)").setValue(Constants.FOCUS);
			controls.getControl("White Balance Temperature, Auto").setValue(Constants.WHITE_BALANCE);
		} catch (ControlException e) {
			e.printStackTrace();
		}
		DEVICE.releaseControlList();
	}
	
}
