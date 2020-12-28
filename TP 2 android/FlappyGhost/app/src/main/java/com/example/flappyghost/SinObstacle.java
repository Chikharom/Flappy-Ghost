package com.example.flappyghost;

import android.media.Image;

public class SinObstacle extends Entity implements Movable{
	
	double y0=0;
	double time =0;
	double w = Math.PI/2;
	
	public SinObstacle(int x, int y) {

			super(x, y, (""+ (int) (Math.random()*27)+""),
					(int) (Math.random()*36+10));
			y0=y;
	}
	
	@Override
	public void move(double dt) {
		
		time+=dt;
		this.posY=(int) (y0+25*Math.sin(w*time));
		setCirclePos();
	}
}
