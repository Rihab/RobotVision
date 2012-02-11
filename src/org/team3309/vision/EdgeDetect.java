package org.team3309.vision;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

public class EdgeDetect {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		FrameGrabber grabber = new OpenCVFrameGrabber(2);
		grabber.setImageWidth(960);
		grabber.setImageHeight(544);
		grabber.start();
		CanvasFrame imageFrame = new CanvasFrame("Image");
		CvMemStorage storage = CvMemStorage.create();
		while(true){
			IplImage grabbed = grabber.grab();
			IplImage gray = IplImage.create(grabbed.width(), grabbed.height(), grabbed.depth(), 1);
			cvCvtColor(grabbed, gray, CV_RGB2GRAY);
			CvSeq edge = new CvSeq();
			cvCanny(gray, gray, 200, 255, 3);
			cvFindContours(gray, storage, edge, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
			while (edge != null && !edge.isNull()) {
                if (edge.elem_size() > 0) {
                    CvSeq points = cvApproxPoly(edge, Loader.sizeof(CvContour.class),
                            storage, CV_POLY_APPROX_DP, cvContourPerimeter(edge)*0.02, 0);
                    cvDrawContours(grabbed, points, CvScalar.BLUE, CvScalar.BLUE, -1, 1, CV_AA);
                }
                edge = edge.h_next();
            }
			imageFrame.showImage(grabbed);
		}
	}

}
