package org.team3309.vision;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

public class TakePics {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Global.init();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		File folder = new File("/home/vmagro/Pictures/RobotVision");
		folder.mkdir();
		FrameGrabber grabber = new OpenCVFrameGrabber(1);
		grabber.start();
		int i = 0;
		String line;
		while(true){
			if(in.ready()){
				line = in.readLine();
				if(line.equals("d"))
					System.exit(1);
				File out = new File(folder, "out"+i+".png");
				ImageIO.write(grabber.grab().getBufferedImage(), "png", out);
				i++;
			}
			grabber.grab().release();
			Thread.sleep(33);
		}
	}

}
