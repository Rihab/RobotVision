package org.team3309.vision;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VisionMain implements CaptureCallback{
	
	private CanvasFrame imageFrame = new CanvasFrame("Image");
	private CanvasFrame threshFrame = new CanvasFrame("Thresh");

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		VisionMain v = new VisionMain();
		Global.init();
		Global.GRABBER.setCaptureCallback(v);
		Global.GRABBER.startCapture();
	}

	@Override
	public void exceptionReceived(V4L4JException e) {
		
	}

	@Override
	public void nextFrame(VideoFrame frame) {
		System.out.println("Received new image "+System.currentTimeMillis());
		IplImage img = IplImage.createFrom(frame.getBufferedImage());
		IplImage hsv = cvCreateImage(cvSize(img.width(), img.height()),
				IPL_DEPTH_8U, 3);
		cvCvtColor(img, hsv, CV_BGR2HSV);
		imageFrame.showImage(hsv);
		IplImage thresh = cvCreateImage(cvSize(img.width(), img.height()),
				IPL_DEPTH_8U, 1);
		cvInRangeS(hsv, cvScalar(0, 0, 87, 0), cvScalar(90, 255, 255, 0), thresh);
		threshFrame.showImage(thresh);
		CvMemStorage storage = CvMemStorage.create();
		CvSeq contours = new CvSeq(null);
		cvFindContours(thresh, storage, contours, Loader.sizeof(CvContour.class),
                CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
		while (contours != null && !contours.isNull()) {
            if (contours.elem_size() > 0) {
                CvSeq points = cvApproxPoly(contours, Loader.sizeof(CvContour.class),
                        storage, CV_POLY_APPROX_DP, cvContourPerimeter(contours)*0.02, 0);
                CvBox2D box = cvMinAreaRect2(contours, storage);
                double ratio = box.size().width() / box.size().height();
                double ideal = 4/3;
                if(Math.abs(ratio - ideal) < .6)
                	if(points.total() == 4){
                		cvDrawContours(img, points, CvScalar.BLUE, CvScalar.BLUE, -1, 1, CV_AA);
                	}
            }
            contours = contours.h_next();
        }
		//imageFrame.showImage(img);
		frame.recycle();
	}

}
