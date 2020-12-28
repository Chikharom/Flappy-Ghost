package com.example.flappyghost;

import android.graphics.Bitmap;
import android.media.Image;

public class Entity{
	
	double posX;
	double posY;
	double posHitCircleX;
	double posHitCircleY;
	String symbol; //sur android
	int radius;
	boolean checked;

	public Entity(int x, int y, String img, int radius) {
		
		this.posX=x;
		this.posY=y;
		this.symbol=img;
		this.radius=radius;
		checked=false;
		setCirclePos();
		
	}
	
	public int getRadius() {
		
		return radius;
	}	
	
	public void setCirclePos(){
		
		//double dx=symbol.getWidth()/2;
		//double dy=symbol.getHeight()/2;
		posHitCircleX=posX+radius;
		posHitCircleY=posY+radius;
		
	}
	
	public void setChecked(boolean toggle) {
		checked=toggle;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setPos(double x, double y) {
		
		posX=x;
		posY=y;
		setCirclePos();
	}
	
	public void deltaPos(double dx, double dy) {
		
		posX+=dx;
		posY+=dy;
		setCirclePos();
	}
	
	public double[] getPos(){
		
		double[] pos = {posX, posY};
		return pos;
	}

	public double[] getPosC(){
		
		double[] pos = {posHitCircleX, posHitCircleY};
		return pos;
	}
	
	public String getImage() {
		
		return this.symbol;
	}
}
