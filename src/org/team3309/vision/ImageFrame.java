package org.team3309.vision;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ImageFrame extends JFrame{
	
	private JLabel content = new JLabel();

	public ImageFrame(String title){
		super(title);
		add(content);
	}
	
	public void showImage(IplImage img){
		showImage(img.getBufferedImage());
	}
	
	public void showImage(BufferedImage img){
		content.setIcon(new ImageIcon(img));
	}
}
