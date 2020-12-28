package com.example.flappyghost;

import java.util.ArrayList;

public class Controler {

	int sizeX;
	int sizeY;
	Game game;
	boolean debug=false;
	double time;

	public Controler(double sizeX, double sizeY){

		game=new Game(sizeX, sizeY);
		this.sizeX=(int)sizeX;
		this.sizeY=(int)sizeY;
		time=System.nanoTime()*1e-9;

	}

	public int[] getDim() {
		
		int[] dim={sizeX, sizeY};
		return dim;
	}
	
	public void setTime(double time) {
		this.time=time;
	}

	public ArrayList<Entity> getObjects() {

		ArrayList<Entity> toDraw=new ArrayList<>();
		toDraw.add(game.getBackground());
		toDraw.add(game.getPlayer());
		
		ArrayList<Entity> obs=game.getObstacles();

		for(int i=0;i<obs.size();i++) {

			toDraw.add(obs.get(i));
		}

		return toDraw;
	}	

	public void checkEnd() {
		if (game.getPlayer().isChecked() && !debug) {
			this.game = new Game(sizeX,sizeY);
		}
	}

	public void setDebug(boolean debug) {
		this.debug=debug;
		this.game.player.setChecked(false);
	}

	public void getCmd(char cmd) {

		if (cmd==' ') {
			game.commandSpace();
		}
	}

	public int getScore() {
		return game.getScore();
	}

	public void nextFrame(double now) {
		game.nextFrame(now-time);
	}
}