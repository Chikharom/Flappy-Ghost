package com.example.flappyghost;

public class MovingObstacle extends Entity implements Movable {
	
	double speedY;
	double speedX;
	
	public MovingObstacle(int x, int y, String img, int radius, double vx, double vy) {
		
		super(x, y, img, radius);
		speedY=vy;
		speedX=vx;
		
	}
	
	@Override
	public void move(double dt) {
		
		deltaPos(speedX*dt, speedY*dt);		
	}
	
	public void setSpeedX(double speed) {
		speedX=speed;
	}
	
	public void setSpeedY(double speed) {
		speedY=speed;
	}
}
