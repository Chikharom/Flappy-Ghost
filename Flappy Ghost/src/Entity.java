import javafx.scene.image.Image;
//classe qui d�finit la logique propre � toute les entit�s
public class Entity{
	
	double posX;//position de l'entit�
	double posY;
	double posHitCircleX;//position du centre du cercle de collision de l'entit�
	double posHitCircleY;
	Image symbol;//image de l'entit�
	int radius;//rayon du hitcircle de l'entit�
	boolean checked;//boolean pour le score/�tat vivant ou mort du joueur

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
	//d�finit le centre de l'entit�
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
	//d�finit la position de l'entit�
	public void setPos(double x, double y) {
		
		posX=x;
		posY=y;
		setCirclePos();
	}
	//d�cale la position de dx et dy
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
