import javafx.scene.image.Image;
//obstacle simple ne se d�pla�ant pas par lui m�me
public class SimpleObstacle extends Entity {
	
	public SimpleObstacle(int x, int y) {
		//l'entit� est cr�e avec une image et un rayon random
		super(x, y, 
				new Image("file:Image\\"+ (int) (Math.random()*27)+".png"),
				(int) (Math.random()*36+10));
	}	
}
