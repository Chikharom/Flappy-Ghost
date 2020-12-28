package com.example.flappyghost;

public class CircularObstacle extends Entity implements Movable {
	
	double speedAng=Math.PI/4;
	double movRadius;
	double counter;
	double phi0;
	int x0;
	int y0;
	
	public CircularObstacle(int x, int y, int radius, double phi0, double movRadius) {
		
		super(x, y, (""+(int)(Math.random()*27)+""), radius);
		x0=x;
		y0=y;
		this.phi0=phi0;
		this.movRadius=movRadius;
		setPos(movRadius*Math.cos(counter*speedAng+phi0)+x0, movRadius*Math.sin(counter*speedAng+phi0)+y0);
		
	}
	
	public double[] getDistance(double x1, double y1, double x2, double y2) {
		double[] dims= {x2-x1, y2-y1};
		return dims;
	}
	
	@Override
	public void move(double dt) {
		
		counter+=dt;
		setPos(movRadius*Math.cos(counter*speedAng+phi0)+x0, movRadius*Math.sin(counter*speedAng+phi0)+y0);

	}
	@Override
	public void deltaPos(double dx, double dy) {
		x0+=dx;
		y0+=dy;
	} 
}
