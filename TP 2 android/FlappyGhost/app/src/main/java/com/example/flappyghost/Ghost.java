package com.example.flappyghost;

import android.media.Image;

public class Ghost extends Entity implements Movable{
	
	double speedX;
	double speedY;
	
	public Ghost(int x, int y) {
		
		super(x, y, "ghost", 30);

	}
	
	public void setSpeedX(double speed) {
		this.speedX=speed;
	}
	public void setSpeedY(double speed) {
		this.speedY=speed;
	}
	public void updateSpeed(double acceleration, double dt) {
		this.speedY+=acceleration*dt;
	}
	@Override
	public void move(double dt) {
		deltaPos(0,speedY*dt);
	}
	
	public double[] getSpeed() {
		double[] speeds= {speedX, speedY};
		return speeds;
	}
	
	public double getDistance(Entity e) {
		
		double dx = posHitCircleX-e.getPosC()[0];
		double dy = posHitCircleY-e.getPosC()[1];
		double distance = Math.sqrt(dx*dx+dy*dy);
		return distance;
	}
	public boolean detectHit(Entity e) {
		
		boolean hit = false;
		
		if (getDistance(e)<e.radius+this.radius) {
			hit = true;
		}
		
		return hit;
	}
}
