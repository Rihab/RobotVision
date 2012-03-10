package org.team3309.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Threshold {
	
	public static int HUE_MIN = 65;
	public static int HUE_MAX = 115;
	public static int SATURATION_MIN = 65;
	public static int SATURATION_MAX = 255;
	public static int VALUE_MIN = 87;
	public static int VALUE_MAX = 255;

	public static void thresholdBackboard(IplImage img, IplImage thresh){
		cvInRangeS(img, cvScalar(HUE_MIN, SATURATION_MIN, VALUE_MIN, 0), cvScalar(HUE_MAX, SATURATION_MAX, VALUE_MAX, 0), thresh);
	}
	
	public static void showSliders(){
		JFrame frame = new JFrame("Thresholding");
		frame.setLayout(new GridLayout(6,2));
		JSlider hueMinSli = new JSlider();
		JSlider hueMaxSli = new JSlider();
		JSlider satMinSli = new JSlider();
		JSlider satMaxSli = new JSlider();
		JSlider valMinSli = new JSlider();
		JSlider valMaxSli = new JSlider();
		
		hueMinSli.setMaximum(127);
		hueMaxSli.setMaximum(127);
		satMinSli.setMaximum(255);
		satMaxSli.setMaximum(255);
		valMinSli.setMaximum(255);
		valMaxSli.setMaximum(255);
		
		hueMinSli.setValue(HUE_MIN);
		hueMaxSli.setValue(HUE_MAX);
		satMinSli.setValue(SATURATION_MIN);
		satMaxSli.setValue(SATURATION_MAX);
		valMinSli.setValue(VALUE_MIN);
		valMaxSli.setValue(VALUE_MAX);
		
		frame.add(new JTextField("Hue min"));
		frame.add(hueMinSli);
		frame.add(new JTextField("Hue max"));
		frame.add(hueMaxSli);
		frame.add(new JTextField("Saturation min"));
		frame.add(satMinSli);
		frame.add(new JTextField("Saturation max"));
		frame.add(satMaxSli);
		frame.add(new JTextField("Value min"));
		frame.add(valMinSli);
		frame.add(new JTextField("Value max"));
		frame.add(valMaxSli);
		
		frame.pack();
		frame.setVisible(true);
		
		
		hueMinSli.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				HUE_MIN = ((JSlider) e.getSource()).getValue();
			}
			
		});
		hueMaxSli.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				HUE_MAX = ((JSlider) e.getSource()).getValue();
			}
			
		});
		satMinSli.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				SATURATION_MIN = ((JSlider) e.getSource()).getValue();
			}
			
		});
		satMaxSli.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				SATURATION_MAX = ((JSlider) e.getSource()).getValue();
			}
			
		});
		valMinSli.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				VALUE_MIN = ((JSlider) e.getSource()).getValue();
			}
			
		});
		valMaxSli.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				VALUE_MAX = ((JSlider) e.getSource()).getValue();
			}
			
		});
	}
	
}
