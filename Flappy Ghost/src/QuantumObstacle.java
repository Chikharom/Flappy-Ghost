import javafx.scene.image.Image;
//obstacle quantique se t�l�portant
public class QuantumObstacle extends Entity implements Movable {
	
	double time = 0;//compteur du temps pour une t�l�portation
	
	public QuantumObstacle(int x, int y) {
		//cr�e l'entit� avec une image random et un rayon random
		super(x, y,
				new Image("file:Image\\"+ (int) (Math.random()*27)+".png"),
				(int) (Math.random()*36+10));
	}
	
	@Override
	public void move(double dt) {
		
		time+=dt;
		//si le temps accumul� est sup�rieur � 0.2 l'obstacle se t�l�porte
		if (time>=0.2) {			
			//modifie la posistion avec x et y compris entre -30 et 30
			deltaPos((int)(Math.random()*61-30), (int)(Math.random()*61-30));
			time=0;
		}
	}
}
