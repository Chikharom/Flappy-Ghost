package com.example.flappyghost;

import java.util.ArrayList;

public class Game {

	int sizeX;
	int sizeY;
	int score=0;
	int obsCounter=0;
	int gravity=500;
	double timeObs;
	Ghost player;
	Entity background=new Entity(0, 0, "bg", 0);
	Entity boss=null;
	boolean bossEvent=false;

	ArrayList<Entity> obstacles=new ArrayList<>();
	ArrayList<Movable> toMove=new ArrayList<>();

	int nextBoss=20;//score � atteindre pour faire appara�tre un nouveau boss

	public Game(double sizeX, double sizeY) {

		this.sizeX=(int)sizeX;
		this.sizeY=(int)sizeY;
		player=new Ghost((int)(sizeX/2)-30, (int)(sizeY/2));
		toMove.add(player);
		player.setSpeedX(120);

	}

	public ArrayList<Entity> getObstacles(){
		return obstacles;
	}

	public Ghost getPlayer() {
		return player;
	}

	public Entity getBoss() {
		return boss;
	}

	public void bossSlide(double dt) {
		
		if (boss!=null) {

			int speedGhost=(int)player.getSpeed()[0];			

			if (bossEvent) {

				if (player.getPos()[0]>sizeX*0.1) {

					for (int i=0; i<toMove.size(); i++) {
						
						((Entity)toMove.get(i)).deltaPos(-speedGhost*dt, 0);
					}
				}
				
			}else {
				
				if (player.getPos()[0]<sizeX*0.5-30) {

					for (int i=0; i<toMove.size(); i++) {
						
						((Entity)toMove.get(i)).deltaPos(speedGhost*dt, 0);
					}
				}
				
			}
		}		
	}
	
	public void slide(double dt) {

		bossSlide(dt);

		for (int i=0; i<toMove.size(); i++) {
			toMove.get(i).move(dt);
		}		

		if (player.getPosC()[1]+player.getRadius()>this.sizeY || player.getPosC()[1]-player.getRadius()<0) {

			player.setSpeedY(-player.getSpeed()[1]);
			player.move(dt);
		}

		int speedLeft=(int)(-player.getSpeed()[0]);

		for (int i=0; i<obstacles.size(); i++) {

			obstacles.get(i).deltaPos(speedLeft*dt, 0);
		}

		player.updateSpeed(gravity, dt);
		background.deltaPos(speedLeft*dt, 0);
		
	}

	
	public void checkObstacles() {
		
		ArrayList<Entity> toDel=new ArrayList<>();
		
		for (int i=0; i<obstacles.size(); i++) {
			
			Entity obs=obstacles.get(i);
			if (obs.getPos()[0]+obs.getRadius()*2<0) {
				
				toDel.add(obs);
			}
		}	
		
		for (int i=0; i<toDel.size(); i++) {
			
			obstacles.remove(toDel.get(i));
			
			
		}
		
		if (boss!=null) {
			
			if (boss.getPos()[0]+boss.getRadius()*2<0) {				
				boss=null;
			}
		}
	}
	
	public void bossFrame(double dt) {
	
		String cmd=((BossPalmier)boss).nextFrame(dt, sizeX, sizeY);
		
		if (cmd.equals("getObs")) {
			
			ArrayList<Entity> toAdd=((BossPalmier)boss).getEventObstacles();
			
			for (int i=0; i<toAdd.size(); i++) {
				obstacles.add(toAdd.get(i));
				toMove.add((Movable)toAdd.get(i));
			}
			
		}else if (cmd.equals("death")) {
			
			score+=30; 
			nextBoss=score+50;
			((BossPalmier)boss).setSpeedX(0);
			((BossPalmier)boss).setSpeedY(200);
			bossEvent=false;
		}
		
	}
	
	public void nextFrame(double dt) {

		if (background.getPos()[0]+sizeX<0) {

			background.setPos(0, 0);
		}	

		if (!bossEvent) {
			addObstacle(dt);
		}
		slide(dt);

		if (bossEvent) {
			bossFrame(dt);
		}

		for (int i=0; i<obstacles.size(); i++) {

			Entity obs=obstacles.get(i);

			if (!obs.isChecked() && obs.getPos()[0]+2*obs.getRadius()<player.getPos()[0] && !bossEvent) {

				score+=5;
				obs.setChecked(true);

				player.setSpeedX((int)(score/10)*15 + 120);
				gravity=(int)(score/10)*15 + 500;

				if (score==nextBoss) {
					startBoss(1);
				}
			}

			if (player.detectHit(obs)) {

				player.setChecked(true);
			}		
		}
		
		checkObstacles();
	}

	public void startBoss(int n) {

		switch (n) {
		case 1: boss=new BossPalmier(sizeX-300+0.4*sizeX, sizeY-270, player.getSpeed()[0]); break;
		}

		boss.setCirclePos();
		((BossPalmier)boss).setSpeedX(player.getSpeed()[0]);
		
		obstacles.clear();
		toMove.clear();
		
		toMove.add(player); toMove.add((Movable)boss);
		Entity[] toAdd=((BossPalmier)boss).getObstacles();
		
		obstacles.add(boss);
		for (int i=0; i<toAdd.length; i++) {
			toMove.add((Movable)toAdd[i]);
			obstacles.add(toAdd[i]);
		}

		bossEvent=true;

	}

	public void commandSpace() {
		player.setSpeedY(-300);
	}

	public int getScore() {
		return score;
	}

	public Entity getBackground() {
		return background;
	}

	public void addObstacle(double dt) {

		timeObs+=dt;

		if (timeObs>3) {

			timeObs=0;
			int rn=(int)(Math.random()*3);
			Entity toAdd=null;
			int randY=(int)(sizeY*Math.random());

			switch(rn) {

			case 0:toAdd=new SimpleObstacle(sizeX+1,randY); break;

			case 1:toAdd=new SinObstacle(sizeX+1,randY);toMove.add((Movable)toAdd); break;

			case 2:toAdd=new QuantumObstacle(sizeX+1,randY);toMove.add((Movable)toAdd); break;

			}
			
			obstacles.add(toAdd);
		}
	}
}