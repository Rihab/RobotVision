package org.team3309.vision;

import java.io.Serializable;

public class DataWrapper implements Serializable{
	
	private int angle = 0;
	private double rpm = 0;
	private boolean canShoot = false;

	public DataWrapper(){
		
	}

	public void setOffAngle(int offAngle) {
		this.angle = offAngle;
	}

	public void setRpm(double rpm) {
		this.rpm = rpm;
	}
	
	public void setCanShoot(boolean canShoot) {
		this.canShoot = canShoot;
	}
}
