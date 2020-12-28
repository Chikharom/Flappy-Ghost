package com.example.flappyghost;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.util.ArrayList;

public class BossPalmier extends Entity implements Movable{
	
	double speedX;
	double speedY;
	MovingObstacle[] sequence=new MovingObstacle[3];
	ArrayList<Entity> eventObstacles=new ArrayList<>();
	int[] eventSequence=new int[3];
	double life=16;
	double counter=0;
	int index=0;
	
	public BossPalmier(double x, double y, double vx) {
		
		super((int)x, (int)y, "palmier", 0);
		radius=135; //moitié de la taille de l'image 270
		speedX=vx;
		
		for (int i=0; i<3; i++) {
			
			eventSequence[i]=(int)(Math.random()*3);
			String img=""+eventSequence[i]+"";
			sequence[i]=new MovingObstacle(0, 0, img, 10, speedX, 0);
		}
		
		sequence[0].setPos(posX+88, posY+95);
		sequence[1].setPos(posX+94, posY+58);
		sequence[2].setPos(posX+123, posY+86);
	}
	
	public MovingObstacle[] getObstacles() {
		
		return sequence;
	}
	
	public ArrayList<Entity> getEventObstacles() {
		
		return eventObstacles;
	}
	
	public void eventWheel(int n) {
		
		eventObstacles.clear();
		for (int i=0; i<4; i++) {
			
			double[] pos=sequence[n].getPos();
			CircularObstacle newC=new CircularObstacle((int)pos[0], (int)pos[1], 12, (((double)i)/4.0)*Math.PI*2, 170);
			eventObstacles.add(newC);
			
		}
	}

	public void eventSpread(int n) {

		eventObstacles.clear();
		double angle=2*Math.PI/3;
		double randN=(int)(Math.random()*2)+4;
		double delAngle=angle/(randN-1);
		double[] pos=sequence[n].getPos(); //les obstacles sont créé en fonction de l'obstacle décoratif

		for (int i=0;i<randN;i++) {

			double[] speed= {20*Math.cos(angle+i*delAngle)+speedX-80, 80*Math.sin(angle+i*delAngle)};
			MovingObstacle newO=new MovingObstacle( (int)pos[0], (int)pos[1], (int)(Math.random()*27)+"", 12, speed[0], speed[1]);
			eventObstacles.add(newO);

		}
	}

	public void eventBoulder() {
		eventObstacles.clear();
		eventObstacles.add(new MovingObstacle((int)posX+70, (int)posY+70, "6", 100, 0, 0));
	}
	
	public String nextFrame(double dt, int sizeX, int sizeY) {
		
		String cmd="";
		life-=dt;
		counter+=dt;
		
		if (life<0) {
			
			return "death"; 
		}		
		
		if (counter>4) {
			
			switch (eventSequence[index]) {
			
			case 0:eventWheel(index);break;
			case 1:eventBoulder();break;
			case 2:eventSpread(index);
			}
			
			index++;
			counter=0;
			cmd="getObs";
			
		}
		return cmd;
	}
	
	public void setSpeedX(double speed) {
		
		speedX=speed;
		
		for (int i=0; i<sequence.length; i++) {
			
			sequence[i].setSpeedX(speed);
		}
	}
	
	public void setSpeedY(double speed) {
		
		speedY=speed;
		
		for (int i=0; i<sequence.length; i++) {
			
			sequence[i].setSpeedY(speed);
		}
	}
	
	@Override
	public void move(double dt) {
		
		deltaPos(speedX*dt, speedY*dt);
	}
}