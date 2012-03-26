package org.team3309.vision;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static org.team3309.vision.Threshold.HUE_MAX;
import static org.team3309.vision.Threshold.HUE_MIN;
import static org.team3309.vision.Threshold.SATURATION_MAX;
import static org.team3309.vision.Threshold.SATURATION_MIN;
import static org.team3309.vision.Threshold.VALUE_MAX;
import static org.team3309.vision.Threshold.VALUE_MIN;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenKinectFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class EdgeDetect{

	private CanvasFrame imageFrame = new CanvasFrame("Image");
	private CanvasFrame depthFrame = new CanvasFrame("Depth");
	private CanvasFrame threshFrame = new CanvasFrame("Edges");


	private static int lowThresh = 50, highThresh = 255;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//Global.init();
		OpenKinectFrameGrabber grabber = new OpenKinectFrameGrabber(
				0);
		grabber.setImageWidth(Constants.CAM_WIDTH);
		grabber.setImageHeight(Constants.CAM_HEIGHT);

		System.out.println(grabber.getImageWidth());
		System.out.println(grabber.getImageHeight());

		JFrame jframe = new JFrame("Threshold");
		JSlider lowSlider = new JSlider();
		JSlider highSlider = new JSlider();
		lowSlider.setMinimum(0);
		highSlider.setMinimum(0);
		lowSlider.setMaximum(255);
		highSlider.setMaximum(255);
		jframe.add(lowSlider, BorderLayout.NORTH);
		jframe.add(highSlider, BorderLayout.SOUTH);
		lowSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				lowThresh = ((JSlider)e.getSource()).getValue();
				System.out.println("Low: "+lowThresh);
			}
		});
		highSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				highThresh = ((JSlider)e.getSource()).getValue();
				System.out.println("High: "+highThresh);
			}
		});
		jframe.pack();
		jframe.setVisible(true);

		EdgeDetect detector = new EdgeDetect();
		
		while(true){
			detector.onRecievedRGBFrame(grabber.grab());
			detector.onRecievedDepthFrame(grabber.grabDepth());
			Thread.sleep(33);
		}
	}

	public void onRecievedDepthFrame(IplImage depth) {
		depthFrame.showImage(depth);
		/*ShortBuffer buf = depth.getShortBuffer();
		int index = (center.y() * depth.width()) + center.x();
		short rawDepth = buf.get(index);
		System.out.println(rawDepth);
		distance = Utils.kinectDistance(rawDepth);*/
	}

	public void onRecievedRGBFrame(IplImage img) {
		IplImage gray = cvCreateImage(
				cvSize(img.width(),
						img.height()), IPL_DEPTH_8U, 1);
		IplImage thresh = cvCreateImage(
				cvSize(img.width(),
						img.height()), IPL_DEPTH_8U, 1);
		cvCvtColor(img, gray, CV_RGB2GRAY);
		cvCanny(gray, thresh, lowThresh, highThresh, 3);
		CvMemStorage storage = CvMemStorage.create();
		CvSeq contours = new CvSeq(null);
		
		//TODO choose between color thresholding or edge detection only
		IplImage hsv = cvCreateImage(
				cvSize(img.width(),
						img.height()), IPL_DEPTH_8U, 3);
		cvCvtColor(img, hsv, CV_RGB2HSV);
		cvInRangeS(hsv,
				cvScalar(HUE_MIN, SATURATION_MIN, VALUE_MIN, 0),
				cvScalar(HUE_MAX, SATURATION_MAX, VALUE_MAX, 0),
				thresh);
		
		cvFindContours(thresh, storage, contours,
				Loader.sizeof(CvContour.class), CV_RETR_TREE,
				CV_CHAIN_APPROX_SIMPLE);
		cvDrawContours(thresh, contours, CvScalar.RED, CvScalar.RED, 1, 1, 8);
		threshFrame.showImage(thresh);
		while (contours != null && !contours.isNull()) {
			if (contours.elem_size() > 0) {
				CvSeq points = cvApproxPoly(contours,
						Loader.sizeof(CvContour.class), storage,
						CV_POLY_APPROX_DP,
						cvContourPerimeter(contours) * 0.02, 0);
				double area = cvContourArea(points, CV_WHOLE_SEQ,
						CV_CLOCKWISE);
				CvRect box = cvBoundingRect(contours, 0);
				CvBox2D minAreaBox = cvMinAreaRect2(contours, storage);
				double boxArea = box.width() * box.height();
				double score = (area / boxArea) * 100;
				score = Math.round(score);
				if (score > 80) {// is probably backboard
					double distance = Utils.distance(box.width());
					distance = Math.round(distance);

					double offCenter = minAreaBox.center().x()
							- (Constants.CAM_WIDTH / 2);
					double offAngle = Math.toDegrees(Math.atan(offCenter
							/ distance));
					
					CvPoint center = cvPoint((int) minAreaBox.center().x(),
							(int) minAreaBox.center().y());
					cvLine(img, center, center, CvScalar.RED, 5, 8, 0);
					System.out.println(offCenter + " " + offAngle);

					cvDrawContours(img, points, CvScalar.BLUE, CvScalar.BLUE, 1, 1, 8);
					
					cvPutText(img,
							String.valueOf(distance),
							center, cvFont(2, 1),
							cvScalar(255, 0, 0, 0));
				}
			}
			contours = contours.h_next();
		}
		cvLine(img, cvPoint(Constants.CAM_WIDTH / 2, 0),
				cvPoint(Constants.CAM_WIDTH / 2, Constants.CAM_HEIGHT),
				CvScalar.RED, 1, 1, 0);
		
		imageFrame.showImage(img);
	}

}
