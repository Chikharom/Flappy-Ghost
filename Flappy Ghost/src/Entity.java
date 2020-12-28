import javafx.scene.image.Image;
//classe qui définit la logique propre à toute les entités
public class Entity{
	
	double posX;//position de l'entité
	double posY;
	double posHitCircleX;//position du centre du cercle de collision de l'entité
	double posHitCircleY;
	Image symbol;//image de l'entité
	int radius;//rayon du hitcircle de l'entité
	boolean checked;//boolean pour le score/état vivant ou mort du joueur

	public Entity(int x, int y, Image img, int radius) {
		
		this.posX=x;
		this.posY=y;
		this.symbol=img;
		this.radius=radius;
		checked=false;
		setCirclePos();
		
	}
	
	//retourne le rayon
	public int getRadius() {
		
		return radius;
	}	
	//définit le centre de l'entité
	public void setCirclePos(){
		
		//double dx=symbol.getWidth()/2;
		//double dy=symbol.getHeight()/2;
		posHitCircleX=posX+radius;
		posHitCircleY=posY+radius;
		
	}
	//set l'attribut checked
	public void setChecked(boolean toggle) {
		checked=toggle;
	}
	//retourne la valeur de checked
	public boolean isChecked() {
		return checked;
	}
	//définit la position de l'entité
	public void setPos(double x, double y) {
		
		posX=x;
		posY=y;
		setCirclePos();
	}
	//décale la position de dx et dy
	public void deltaPos(double dx, double dy) {
		
		posX+=dx;
		posY+=dy;
		setCirclePos();
	}
	//retourne la position
	public double[] getPos(){
		
		double[] pos = {posX, posY};
		return pos;
	}
	//retourne la position du centre du cercle
	public double[] getPosC(){
		
		double[] pos = {posHitCircleX, posHitCircleY};
		return pos;
	}
	//retourne l'image
	public Image getImage() {
		
		return this.symbol;
	}
}
