import javafx.scene.image.Image;

//l'obstacle mouvant peut se diriger en ligne droite en fontion de la vitesse
public class MovingObstacle extends Entity implements Movable {
	
	double speedY;//vitesse de l'obstacle mouvant
	double speedX;
	
	public MovingObstacle(int x, int y, Image img, int radius, double vx, double vy) {
		
		//construit l'entité
		super(x, y, img, radius);
		speedY=vy;
		speedX=vx;
		
	}
	
	@Override
	public void move(double dt) {
		//modifie la position en fonction de la vitesse et de dt
		deltaPos(speedX*dt, speedY*dt);		
	}
	//définit la vitesse en x
	public void setSpeedX(double speed) {
		speedX=speed;
	}
	//définit la vitesse en y
	public void setSpeedY(double speed) {
		speedY=speed;
	}
}
