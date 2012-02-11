package org.team3309.vision;

import java.util.List;

import au.edu.jcu.v4l4j.Control;
import au.edu.jcu.v4l4j.ControlList;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.exceptions.ControlException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class Global {
	
	public static VideoDevice DEVICE;

	public static void init(){
		try {
			DEVICE = new VideoDevice(Constants.DEVICE);
		} catch (V4L4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setSettings();
	}
	
	private static void setSettings(){
		ControlList controls = DEVICE.getControlList();
		try {
			controls.getControl("Contrast").setValue(Constants.CONTRAST);
		} catch (ControlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
