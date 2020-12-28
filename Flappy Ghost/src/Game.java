import java.util.ArrayList;

import javafx.scene.image.Image;
//classe principale du mod�le g�rant les interactions sur le "plateau" du jeu
public class Game {

	int sizeX;//dimensions du jeu
	int sizeY;
	int score=0;
	int gravity=500;
	double timeObs; //compteur de temps pour le prochain Obstacle
	Ghost player;
	Entity background=new Entity(0, 0, new Image("file:Image\\bg.png"), 0);
	Entity boss=null;//le boss actuel pour le premier bonus
	boolean bossEvent=false;//si un boss est pr�sent

	ArrayList<Entity> obstacles=new ArrayList<>(); //liste d'obstacles
	ArrayList<Movable> toMove=new ArrayList<>(); //liste d'objet mouvants

	int nextBoss=20;//score � atteindre pour faire appara�tre un nouveau boss

	public Game(double sizeX, double sizeY) {

		this.sizeX=(int)sizeX;
		this.sizeY=(int)sizeY;
		//d�finit le joueur
		player=new Ghost((int)(sizeX/2)-30, (int)(sizeY/2));
		toMove.add(player);
		player.setSpeedX(120);

	}

	//retourne les obstacles pr�sent
	public ArrayList<Entity> getObstacles(){
		return obstacles;
	}

	//retourne le joueur 
	public Ghost getPlayer() {
		return player;
	}

	//retourne le boss
	public Entity getBoss() {
		return boss;
	}

	//s'occupe du mouvement sp�cial de translation lors de l'apparition et la disparition d'un boss
	public void bossSlide(double dt) {
		
		if (boss!=null) { //event actif seulement en pr�sence d'un boss

			int speedGhost=(int)player.getSpeed()[0];			

			if (bossEvent) { //si il y a un bossEvent le boss est encore en vie, il faut d�caller le joueur � gauche

				//si le joueur est trop � droite le d�cale � gauche
				if (player.getPos()[0]>sizeX*0.1) {

					for (int i=0; i<toMove.size(); i++) {
						
						((Entity)toMove.get(i)).deltaPos(-speedGhost*dt, 0);
					}
				}
				
			}else { //le boss est mort, il faut d�caler le joueur � droite
				
				//si le joueur est trop � gauche le d�calle � droite
				if (player.getPos()[0]<sizeX*0.5-30) {

					//d�calle tout les obstacles mouvants en m�me temps que le joueur
					for (int i=0; i<toMove.size(); i++) {
						
						((Entity)toMove.get(i)).deltaPos(speedGhost*dt, 0);
					}
				}
				
			}
		}		
	}
	
	//s'occupe du mouvement des entit�
	public void slide(double dt) {

		bossSlide(dt);

		//d�place chaque entit� mouvante
		for (int i=0; i<toMove.size(); i++) {
			toMove.get(i).move(dt);
		}		

		//g�re le rebond au plafond et au sol du fant�me
		if (player.getPosC()[1]+player.getRadius()>this.sizeY || player.getPosC()[1]-player.getRadius()<0) {

			player.setSpeedY(-player.getSpeed()[1]);//inverse la vitesse lors d'un rebond
			player.move(dt); //bouge le joueur
		}

		int speedLeft=(int)(-player.getSpeed()[0]);//vitesse � laquelle les objets translates � gauche

		//fait translater tout les objets
		for (int i=0; i<obstacles.size(); i++) {

			obstacles.get(i).deltaPos(speedLeft*dt, 0);
		}

		player.updateSpeed(gravity, dt); //change la vitesse du joueur avec l'acc�leration en y
		background.deltaPos(speedLeft*dt, 0);
		
	}

	//v�rifie si les obstacles doivent �tre retir�
	public void checkObstacles() {
		
		ArrayList<Entity> toDel=new ArrayList<>();
		
		for (int i=0; i<obstacles.size(); i++) {
			
			Entity obs=obstacles.get(i);
			//si l'obstacle est en dehors de l'�cran il sera retir�
			if (obs.getPos()[0]+obs.getRadius()*2<0) {
				
				toDel.add(obs);
			}
		}	
		
		//supression de chaque obstacle
		for (int i=0; i<toDel.size(); i++) {
			
			obstacles.remove(toDel.get(i));			
		}
		
		//v�rifie la position du boss
		if (boss!=null) {
			
			if (boss.getPos()[0]+boss.getRadius()*2<0) {				
				boss=null;
			}
		}
	}
	
	//action du boss effectuer � chaque frame
	public void bossFrame(double dt) {
	
		//commande re�u de la part du boss
		String cmd=((BossPalmier)boss).nextFrame(dt, sizeX, sizeY);
		
		//va chercher les obstacles cr�er dans par le boss
		if (cmd.equals("getObs")) {
			
			ArrayList<Entity> toAdd=((BossPalmier)boss).getEventObstacles();
			
			//ajoute les obstacles du boss � la liste d'obstacles
			for (int i=0; i<toAdd.size(); i++) {
				
				obstacles.add(toAdd.get(i));
				toMove.add((Movable)toAdd.get(i));
			}
			
		//d�sactive le bossEvent et pr�pare sont �limination	
		}else if (cmd.equals("death")) {
			
			score+=30; 
			nextBoss=score+50;
			((BossPalmier)boss).setSpeedX(0);
			((BossPalmier)boss).setSpeedY(200);
			bossEvent=false;
		}
		
	}
	
	//activ� � chaque frame
	public void nextFrame(double dt) {

		//v�rifie si le background est "sorti" de l'�cran
		if (background.getPos()[0]+sizeX<0) {

			//remet le background � 0
			background.setPos(0, 0);
		}	

		//si il n'y a pas de boss tente d'ajouter un obstacle
		if (!bossEvent) {
			addObstacle(dt);
		}
		slide(dt);

		if (bossEvent) {
			bossFrame(dt);
		}

		//v�rifie pour chaque obstacle si il � �t� d�pass�
		for (int i=0; i<obstacles.size(); i++) {

			Entity obs=obstacles.get(i);

			//ajoute le score si un obstacle est d�pass�
			if (!obs.isChecked() && obs.getPos()[0]+2*obs.getRadius()<player.getPos()[0] && !bossEvent) {

				score+=5;
				obs.setChecked(true);

				//met � jour la gravit� et la vitesse
				player.setSpeedX((int)(score/10)*15 + 120);
				gravity=(int)(score/10)*15 + 500; 

				//ajoute le prochain boss
				if (score==nextBoss) {
					startBoss(1);
				}
			}

			//si il y a une collision entre l'obstacle et le joueur
			if (player.detectHit(obs)) {

				player.setChecked(true);
			}		
		}
		
		//appel � la v�rification de la position des obstacles
		checkObstacles();
	}

	//cr�er un boss en fonction du num�ro rentr�
	public void startBoss(int n) {

		
		switch (n) {
		//cr�e un BossPalmier 
		case 1: boss=new BossPalmier(sizeX-300+0.4*sizeX, sizeY-270, player.getSpeed()[0]); break;
		}

		boss.setCirclePos(); //d�fini sont centre puis sa vitesse
		((BossPalmier)boss).setSpeedX(player.getSpeed()[0]);
		
		obstacles.clear(); //supprime les obstacles pr�sents
		toMove.clear();//supprime les obtsacles de la liste de mouvement
		
		toMove.add(player); toMove.add((Movable)boss);
		Entity[] toAdd=((BossPalmier)boss).getObstacles();//r�cup�re les obstacles d�coratif		
		obstacles.add(boss);
		
		//ajoute les obstacles aux listes
		for (int i=0; i<toAdd.length; i++) {
			
			toMove.add((Movable)toAdd[i]);
			obstacles.add(toAdd[i]);
		}

		bossEvent=true;

	}

	//fait "sauter" le joueur
	public void commandSpace() {
		player.setSpeedY(-300);
	}

	//retourne le score
	public int getScore() {
		return score;
	}

	//retourne le background
	public Entity getBackground() {
		return background;
	}

	//tente d'ajouter un obstacle
	public void addObstacle(double dt) {

		timeObs+=dt;//compte le temps jusqu'au prochain obstacle
		//ajoute l'obstacle au bout de 3 secondes
		if (timeObs>3) {

			timeObs=0;
			int rn=(int)(Math.random()*3);//obstacle random parmi 3
			Entity toAdd=null;//entit� qui sera ajouter � la liste
			int randY=(int)(sizeY*Math.random()); //la position al�atoire en y

			switch(rn) {//d�finit toAdd en fonction de rn

			case 0:toAdd=new SimpleObstacle(sizeX+1,randY); break;
			//ajoute l'obstacle sinus aux movable
			case 1:toAdd=new SinObstacle(sizeX+1,randY);toMove.add((Movable)toAdd); break;
			//ajoute l'obstacle quantique aux movavle
			case 2:toAdd=new QuantumObstacle(sizeX+1,randY);toMove.add((Movable)toAdd); break;

			}
			//ajoute l'obstacle � la liste principale
			obstacles.add(toAdd);
		}
	}
}