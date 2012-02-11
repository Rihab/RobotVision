package org.team3309.vision;

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvDrawContours;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_POLY_APPROX_DP;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_LIST;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvApproxPoly;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvContourPerimeter;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMinAreaRect2;

import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvBox2D;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class VisionMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		FrameGrabber grabber = new OpenCVFrameGrabber(2);
		grabber.setImageWidth(960);
		grabber.setImageHeight(544);
		grabber.start();
		CanvasFrame imageFrame = new CanvasFrame("Image");
		CanvasFrame threshFrame = new CanvasFrame("Thresh");
		while(true){
			IplImage img = grabber.grab();
			IplImage hsv = cvCreateImage(cvSize(img.width(), img.height()),
					IPL_DEPTH_8U, 3);
			cvCvtColor(img, hsv, CV_BGR2HSV);
			int increase = 255;
			//increaseSaturation(hsv, increase);
			IplImage thresh = cvCreateImage(cvSize(img.width(), img.height()),
					IPL_DEPTH_8U, 1);
			cvInRangeS(hsv, cvScalar(85, 0, 160, 0), cvScalar(100, 255, 255, 0), thresh);
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
			imageFrame.showImage(img);
			Thread.sleep(33);
		}
	}
	
	private static void increaseSaturation(IplImage img, int value){
		int heightc = img.height();
		int widthc = img.width();
		int stepc = img.widthStep();
		int channelsc = img.nChannels();
		BytePointer datac = img.imageData();
		for(int i=0;i< (heightc);i++) for(int j=0;j<(widthc);j++)
		{/*Here datac means data of the HSV Image*/
		/*Here i want to Increase the saturation or the strength of the Colors in the Image and
		then I would be able to perform a good color detection*/

		int temp=datac.get(i*stepc+j*channelsc+1)+value;/*increase the saturation component is the second arrray.*/

		/*Here there is a small problem...when you add a value to the data and if it exceeds 255
		it starts all over again from zero and hence some of the pixels might go to zero.
		So to stop this we need to include this loop i would not try to explain the loop but
		please try and follow it is easy to do so..*/
		if(temp>255) datac.put(i*stepc+j*channelsc+1, (byte) 255);
		else datac.put(i*stepc+j*channelsc+1, (byte) temp);/*you may
		please remove and see what is happening if the if else loop is not there*/}
	}

}
