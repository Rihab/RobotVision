package org.team3309.vision;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VisionMain{
	
	private static CanvasFrame imageFrame = new CanvasFrame("Image");
	private static CanvasFrame threshFrame = new CanvasFrame("Thresh");
	//private static CanvasFrame hsvFrame = new CanvasFrame("hsv");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Global.init();
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(Constants.DEVICE_NUM);
		grabber.setImageWidth(Constants.CAM_WIDTH);
		grabber.setImageHeight(Constants.CAM_HEIGHT);
		
		System.out.println(grabber.getImageWidth());
		System.out.println(grabber.getImageHeight());
		
		grabber.start();
		while(true){
			IplImage img = grabber.grab();
			IplImage hsv = cvCreateImage(cvSize(img.width(), img.height()), IPL_DEPTH_8U, 3);
			cvCvtColor(img, hsv, CV_RGB2HSV);
			IplImage thresh = Threshold.thresholdBackboard(hsv);
			
			threshFrame.showImage(thresh);
			CvMemStorage storage = CvMemStorage.create();
			CvSeq contours = new CvSeq(null);
			cvFindContours(thresh, storage, contours, Loader.sizeof(CvContour.class),
	                CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
			while (contours != null && !contours.isNull()) {
	            if (contours.elem_size() > 0) {
	                CvSeq points = cvApproxPoly(contours, Loader.sizeof(CvContour.class),
	                        storage, CV_POLY_APPROX_DP, cvContourPerimeter(contours)*0.02, 0);
	            	double area = cvContourArea(points, CV_WHOLE_SEQ, CV_CLOCKWISE);
	                CvRect box = cvBoundingRect(contours, 0);
	                CvBox2D minAreaBox = cvMinAreaRect2(contours, storage);
	                double boxArea = box.width() * box.height();
	                double score = (area / boxArea) * 100;
	                score = Math.round(score);
	                if(score > 75){//is probably backboard
	                	double distance = Utils.distance(box.width());
	                	cvPutText(img, String.valueOf(distance), cvPoint(box.x(), box.y()), cvFont(2, 1), cvScalar(255, 0, 0, 0));
	                	double offCenter = minAreaBox.center().x() - (Constants.CAM_WIDTH/2);
	                	double offAngle = Math.toDegrees(Math.atan(offCenter / distance));
						cvLine(img,
								cvPoint((int) minAreaBox.center().x(),
										(int) minAreaBox.center().y()),
								cvPoint((int) minAreaBox.center().x(),
										(int) minAreaBox.center().y()),
								CvScalar.RED, 5, 8, 0);
						//System.out.println(offCenter + " " + offAngle);
	                }
	            }
	            contours = contours.h_next();
	        }
			cvLine(img,
					cvPoint(Constants.CAM_WIDTH/2,0),
					cvPoint(Constants.CAM_WIDTH/2,Constants.CAM_HEIGHT),
					CvScalar.RED, 5, 8, 0);
			imageFrame.showImage(img);
			Thread.sleep(33);
		}
	}

}
