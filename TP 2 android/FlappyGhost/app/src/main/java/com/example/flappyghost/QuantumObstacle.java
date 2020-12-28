package com.example.flappyghost;

public class QuantumObstacle extends Entity implements Movable {
	
	double time = 0;
	
	public QuantumObstacle(int x, int y) {
		
		super(x, y,
				(""+ (int) (Math.random()*27)+""),
				(int) (Math.random()*36+10));
	}
	
	@Override
	public void move(double dt) {
		
		time+=dt;
		
		if (time>=0.2) {			
			
			deltaPos((int)(Math.random()*61-30), (int)(Math.random()*61-30));
			time=0;
		}
	}
}
