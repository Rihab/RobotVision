package org.team3309.vision;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenKinectFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class KinectTest {
	
	public static void main(String[] args) throws Exception{
		OpenKinectFrameGrabber kinect = new OpenKinectFrameGrabber(0);
		CanvasFrame frame = new CanvasFrame("Kinect");
		CanvasFrame depthFrame = new CanvasFrame("Depth");
		kinect.start();
		while(true){
			IplImage img = kinect.grab();
			IplImage depth = kinect.grabDepth();
			frame.showImage(img);
			depthFrame.showImage(depth);
			Thread.sleep(33);
		}
	}

}
