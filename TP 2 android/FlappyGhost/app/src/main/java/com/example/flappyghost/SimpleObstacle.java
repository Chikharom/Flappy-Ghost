package com.example.flappyghost;

public class SimpleObstacle extends Entity {
	
	public SimpleObstacle(int x, int y) {
		
		super(x, y, 
				(""+ (int) (Math.random()*27)+""),
				(int) (Math.random()*36+10));
	}	
}
