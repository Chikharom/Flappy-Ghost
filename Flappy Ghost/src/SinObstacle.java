import javafx.scene.image.Image;
//obstacle se déplaçant vercialement en fonction d'une fonction sinus
public class SinObstacle extends Entity implements Movable{

	double y0=0;//y0 du sinus
	double time =0;//compteur de temps pour le sinus
	double w = Math.PI/2;//vitesse angulaire de l'obstacle 

	public SinObstacle(int x, int y) {
		//crée l'entité avec une image et un rayon random
		super(x, y, 
				new Image("file:Image\\"+ (int) (Math.random()*27)+".png"),
				(int) (Math.random()*36+10));
		y0=y;
	}

	@Override
	public void move(double dt) {

		time+=dt;//incrémente le compteur puis modifie la position 
		this.posY=(int) (y0+25*Math.sin(w*time));
		setCirclePos();
	}
}
