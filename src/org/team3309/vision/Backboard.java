package org.team3309.vision;

import java.awt.Point;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

public class Backboard {
	
	public static final int TOP = 0; 
	public static final int MID_LEFT = 1;
	public static final int MID_RIGHT = 2;
	public static final int BOTTOM = 3;
	
	private CvPoint center;
	private int score;
	
	public Backboard(CvPoint center, int score){
		this.center = center;
		this.score = score;
	}
	
	public CvPoint getCenterCv(){
		return center;
	}
	
	public Point getCenter(){
		return new Point(center.x(), center.y());
	}
	
	public static class BackboardArray{
		
		private Backboard[] array = new Backboard[4];
		
		public BackboardArray(Backboard b1, Backboard b2, Backboard b3, Backboard b4){
			array[0] = b1;
			array[1] = b2;
			array[2] = b3;
			array[3] = b4;
			order();
		}
		
		private void order(){
			Backboard[] temp = new Backboard[4];
			int highest = Constants.CAM_HEIGHT;
			int leftmost = Constants.CAM_WIDTH;
			int rightmost = 0;
			int lowest = 0;
			for(int i=0; i<array.length; i++){
				Backboard current = array[i];
				if(current.center.y() < highest){//less than because image starts with (0,0) in the top left
					highest = current.center.y();
					temp[TOP] = current;
				}
				if(current.center.x() < leftmost){//less than because image starts with (0,0) in the top left
					leftmost = current.center.x();
					temp[MID_LEFT] = current;
				}
				if(current.center.x() > rightmost){
					rightmost = current.center.x();
					temp[MID_RIGHT] = current;
				}
				if(current.center.y() > lowest){
					lowest = current.center.x();
					temp[BOTTOM] = current;
				}
			}
			array = temp;
		}
		
	}

}
