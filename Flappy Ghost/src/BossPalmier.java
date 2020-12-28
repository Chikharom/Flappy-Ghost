import javafx.scene.image.Image;
import java.util.ArrayList;

public class BossPalmier extends Entity implements Movable{
	
	double speedX;//vitesse du boss
	double speedY;
	MovingObstacle[] sequence=new MovingObstacle[3];//sequence d'obstacle d�coratif
	ArrayList<Entity> eventObstacles=new ArrayList<>();//liste d'obstacle de l'event
	int[] eventSequence=new int[3];//array qui d�finit la s�quence d'event
	double life=16;//la dur�e du boss en seconde
	double counter=0;//coompteur de temps pour le d�but d'event
	int index=0;//event actuel
	
	public BossPalmier(double x, double y, double vx) {
		//l'entit� est cr�� avec l'image du palmier
		super((int)x, (int)y, new Image("file:Image\\Palmier.png"), 0);
		Image palm=new Image("file:Image\\Palmier.png");
		//le rayon vaut la moiti� de la largeur de l'image
		radius=(int)(palm.getHeight()/2);
		speedX=vx;
		
		//boucle cr�ant la s�quence d'obstacle
		for (int i=0; i<3; i++) {
			
			eventSequence[i]=(int)(Math.random()*3);//l'event prochain est al�atoire
			Image img=new Image("file:Image\\"+eventSequence[i]+".png"); //l'image d�pent de l'event qui sera lanc�
			sequence[i]=new MovingObstacle(0, 0, img, 10, speedX, 0); //ajoute l'obstacle d�coratif 
		}
		
		sequence[0].setPos(posX+88, posY+95);//les obstacles sont position�s
		sequence[1].setPos(posX+94, posY+58);
		sequence[2].setPos(posX+123, posY+86);
	}
	
	//retourne les obstacles d�coratif
	public MovingObstacle[] getObstacles() {
		
		return sequence;
	}
	//retourne les obstacles de l'�v�nement
	public ArrayList<Entity> getEventObstacles() {
		
		return eventObstacles;
	}
	//�v�nement cr�ant 4 circularObstacles 
	public void eventWheel(int n) {
		
		//reset les obstacles actuels
		eventObstacles.clear();
		double[] pos=sequence[n].getPos(); //les obstacles sont cr�� en fonction de l'obstacle mouvant
		
		for (int i=0; i<4; i++) {
			
			CircularObstacle newC=new CircularObstacle((int)pos[0], (int)pos[1], 12, (((double)i)/4.0)*Math.PI*2, 170);
			eventObstacles.add(newC);			
		}		
	}
	
	//cr�e un "�ventail" d'obstacles 
	public void eventSpread(int n) {
		
		eventObstacles.clear();
		double angle=2*Math.PI/3;
		double randN=(int)(Math.random()*2)+4;
		double delAngle=angle/(randN-1);
		double[] pos=sequence[n].getPos(); //les obstacles sont cr�� en fonction de l'obstacle d�coratif
		
		//boucle ajoutant les obstacles 
		for (int i=0;i<randN;i++) {
			//la vitesse est d�fini en fonction de l'angle et se d�place 80 px/s moins vite que le palmier
			double[] speed= {20*Math.cos(angle+i*delAngle)+speedX-80, 80*Math.sin(angle+i*delAngle)};
			Image img=new Image("file:Image\\"+(int)(Math.random()*27)+".png"); //l'image de l'obstacle est random.
			MovingObstacle newO=new MovingObstacle( (int)pos[0], (int)pos[1], img, 12, speed[0], speed[1]);
			eventObstacles.add(newO);
			
		}
	}
	
	//event cr�ant un obstacle "g�ant" au sol
	public void eventBoulder() {
		
		eventObstacles.clear();
		Image img=new Image("file:Image\\1.png");
		eventObstacles.add(new MovingObstacle((int)posX+70, (int)posY+70, img, 100, 0, 0));
	}
	
	//m�thode appel� � chaque frame 
	public String nextFrame(double dt, int sizeX, int sizeY) {
		
		String cmd="";//la commande qui sera retourn� au game par le boss
		life-=dt;//la "vie" du boss diminue
		counter+=dt;
		
		if (life<0) {
			
			return "death"; //signale que le boss est vaincu
		}		
		
		if (counter>4) {//active un nouvel �v�nement
			
			switch (eventSequence[index]) {
			
			case 0:eventWheel(index);break;
			case 1:eventBoulder();break;
			case 2: eventSpread(index);
			}
			
			index++;//incr�mente l'index d'event
			counter=0;
			cmd="getObs";//signale au game qu'un nouvel event est d�clench� pour r�cup�rer les obstacles cr��
			
		}
		return cmd;
	}
	
	//d�finit la vitesse en x du boss
	public void setSpeedX(double speed) {
		
		speedX=speed;
		//modifie la vitesse des obstacles d�coratif 
		for (int i=0; i<sequence.length; i++) {
			
			sequence[i].setSpeedX(speed);
		}
	}
	//d�finit la vitesse en y du boss
	public void setSpeedY(double speed) {
		
		speedY=speed;
		//d�finit la vitesse des obstacles d�coratif
		for (int i=0; i<sequence.length; i++) {
			
			sequence[i].setSpeedY(speed);
		}
	}
	//d�place le boss
	@Override
	public void move(double dt) {
		
		deltaPos(speedX*dt, speedY*dt);
	}
}