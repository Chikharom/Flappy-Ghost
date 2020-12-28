import javafx.scene.image.Image;
//obstacle qui se d�place circulairement autour d'un point centrale
public class CircularObstacle extends Entity implements Movable {
	
	double speedAng=Math.PI/4; //vitesse angulaire
	double movRadius;//rayon du centre jusqu'� l'obstacle
	double counter;//compteur de temps
	double phi0;//angle � t=0
	int x0;
	int y0;
	
	public CircularObstacle(int x, int y, int radius, double phi0, double movRadius) {
		//cr�e l'entit� avec une image random
		super(x, y, new Image("file:Image/"+(int)(Math.random()*27)+".png"), radius);
		x0=x;
		y0=y;
		this.phi0=phi0;
		this.movRadius=movRadius;
		//d�finit la position en fonction du rayon et de l'angle
		setPos(movRadius*Math.cos(counter*speedAng+phi0)+x0, movRadius*Math.sin(counter*speedAng+phi0)+y0);
		
	}
	
	@Override
	public void move(double dt) {
		//incr�mente le compteur et red�fini la position
		counter+=dt;
		setPos(movRadius*Math.cos(counter*speedAng+phi0)+x0, movRadius*Math.sin(counter*speedAng+phi0)+y0);

	}
	//modifie deltaPos pour faire bouger le centre de rotation plut�t que l'obstacle
	@Override
	public void deltaPos(double dx, double dy) {
		x0+=dx;
		y0+=dy;
	} 
}
