import javafx.scene.image.Image;
import java.util.ArrayList;

public class BossPalmier extends Entity implements Movable{
	
	double speedX;//vitesse du boss
	double speedY;
	MovingObstacle[] sequence=new MovingObstacle[3];//sequence d'obstacle décoratif
	ArrayList<Entity> eventObstacles=new ArrayList<>();//liste d'obstacle de l'event
	int[] eventSequence=new int[3];//array qui définit la séquence d'event
	double life=16;//la durée du boss en seconde
	double counter=0;//coompteur de temps pour le début d'event
	int index=0;//event actuel
	
	public BossPalmier(double x, double y, double vx) {
		//l'entité est créé avec l'image du palmier
		super((int)x, (int)y, new Image("file:Image\\Palmier.png"), 0);
		Image palm=new Image("file:Image\\Palmier.png");
		//le rayon vaut la moitié de la largeur de l'image
		radius=(int)(palm.getHeight()/2);
		speedX=vx;
		
		//boucle créant la séquence d'obstacle
		for (int i=0; i<3; i++) {
			
			eventSequence[i]=(int)(Math.random()*3);//l'event prochain est aléatoire
			Image img=new Image("file:Image\\"+eventSequence[i]+".png"); //l'image dépent de l'event qui sera lancé
			sequence[i]=new MovingObstacle(0, 0, img, 10, speedX, 0); //ajoute l'obstacle décoratif 
		}
		
		sequence[0].setPos(posX+88, posY+95);//les obstacles sont positionés
		sequence[1].setPos(posX+94, posY+58);
		sequence[2].setPos(posX+123, posY+86);
	}
	
	//retourne les obstacles décoratif
	public MovingObstacle[] getObstacles() {
		
		return sequence;
	}
	//retourne les obstacles de l'événement
	public ArrayList<Entity> getEventObstacles() {
		
		return eventObstacles;
	}
	//événement créant 4 circularObstacles 
	public void eventWheel(int n) {
		
		//reset les obstacles actuels
		eventObstacles.clear();
		double[] pos=sequence[n].getPos(); //les obstacles sont créé en fonction de l'obstacle mouvant
		
		for (int i=0; i<4; i++) {
			
			CircularObstacle newC=new CircularObstacle((int)pos[0], (int)pos[1], 12, (((double)i)/4.0)*Math.PI*2, 170);
			eventObstacles.add(newC);			
		}		
	}
	
	//crée un "éventail" d'obstacles 
	public void eventSpread(int n) {
		
		eventObstacles.clear();
		double angle=2*Math.PI/3;
		double randN=(int)(Math.random()*2)+4;
		double delAngle=angle/(randN-1);
		double[] pos=sequence[n].getPos(); //les obstacles sont créé en fonction de l'obstacle décoratif
		
		//boucle ajoutant les obstacles 
		for (int i=0;i<randN;i++) {
			//la vitesse est défini en fonction de l'angle et se déplace 80 px/s moins vite que le palmier
			double[] speed= {20*Math.cos(angle+i*delAngle)+speedX-80, 80*Math.sin(angle+i*delAngle)};
			Image img=new Image("file:Image\\"+(int)(Math.random()*27)+".png"); //l'image de l'obstacle est random.
			MovingObstacle newO=new MovingObstacle( (int)pos[0], (int)pos[1], img, 12, speed[0], speed[1]);
			eventObstacles.add(newO);
			
		}
	}
	
	//event créant un obstacle "géant" au sol
	public void eventBoulder() {
		
		eventObstacles.clear();
		Image img=new Image("file:Image\\1.png");
		eventObstacles.add(new MovingObstacle((int)posX+70, (int)posY+70, img, 100, 0, 0));
	}
	
	//méthode appelé à chaque frame 
	public String nextFrame(double dt, int sizeX, int sizeY) {
		
		String cmd="";//la commande qui sera retourné au game par le boss
		life-=dt;//la "vie" du boss diminue
		counter+=dt;
		
		if (life<0) {
			
			return "death"; //signale que le boss est vaincu
		}		
		
		if (counter>4) {//active un nouvel événement
			
			switch (eventSequence[index]) {
			
			case 0:eventWheel(index);break;
			case 1:eventBoulder();break;
			case 2: eventSpread(index);
			}
			
			index++;//incrémente l'index d'event
			counter=0;
			cmd="getObs";//signale au game qu'un nouvel event est déclenché pour récupérer les obstacles créé
			
		}
		return cmd;
	}
	
	//définit la vitesse en x du boss
	public void setSpeedX(double speed) {
		
		speedX=speed;
		//modifie la vitesse des obstacles décoratif 
		for (int i=0; i<sequence.length; i++) {
			
			sequence[i].setSpeedX(speed);
		}
	}
	//définit la vitesse en y du boss
	public void setSpeedY(double speed) {
		
		speedY=speed;
		//définit la vitesse des obstacles décoratif
		for (int i=0; i<sequence.length; i++) {
			
			sequence[i].setSpeedY(speed);
		}
	}
	//déplace le boss
	@Override
	public void move(double dt) {
		
		deltaPos(speedX*dt, speedY*dt);
	}
}