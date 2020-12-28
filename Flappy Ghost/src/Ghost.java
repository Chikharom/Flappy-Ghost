import javafx.scene.image.Image;

//classe qui g�re la logique propre au fant�me, dont les collision et le d�placement
public class Ghost extends Entity implements Movable{
	
	double speedX;//vitesse du fant�me
	double speedY;
	
	public Ghost(int x, int y) {

		//construit l'entit�
		super(x, y,
				new Image("file:Image/ghost.png"), 30);

	}
	//d�finit la vitesse en x
	public void setSpeedX(double speed) {
		this.speedX=speed;
	}
	//d�finit la vitesse en y
	public void setSpeedY(double speed) {
		this.speedY=speed;
	}
	//modifie la vitesse en fonction de l'acceleration
	public void updateSpeed(double acceleration, double dt) {
		this.speedY+=acceleration*dt;
	}
	//op�ration de mouvement par le fant�me
	@Override
	public void move(double dt) {
		deltaPos(0,speedY*dt);
	}
	//retourne la vitesse du fant�me
	public double[] getSpeed() {
		double[] speeds= {speedX, speedY};
		return speeds;
	}
	//retourne la distance entre le fant�me et l'entit�
	public double getDistance(Entity e) {
		
		double dx = posHitCircleX-e.getPosC()[0];
		double dy = posHitCircleY-e.getPosC()[1];
		double distance = Math.sqrt(dx*dx+dy*dy);
		return distance;
	}
	//retourne vrai si le fant�me est en collision avec l'entit�
	public boolean detectHit(Entity e) {
		
		boolean hit = false;
		//la somme des deux rayon est la plus petite distance possible sans collision
		if (getDistance(e)<e.radius+this.radius) {
			hit = true;
		}
		
		return hit;
	}
}
