import java.util.ArrayList;

public class Controler {

	int sizeX; //les dimensions du contrôleur
	int sizeY;
	Game game; //le jeu 
	boolean debug=false;
	double time; //le temps précédent

	public Controler(double sizeX, double sizeY){

		game=new Game(sizeX, sizeY);//création du jeu
		this.sizeX=(int)sizeX;
		this.sizeY=(int)sizeY;
		time=System.nanoTime()*1e-9;

	}

	//retourne les dimensions 
	public int[] getDim() {
		
		int[] dim={sizeX, sizeY};
		return dim;
	}
	
	//définie le temps actuel
	public void setTime(double time) {
		this.time=time;
	}

	//retourne tout les objets à dessiner
	public ArrayList<Entity> getObjects() {

		ArrayList<Entity> toDraw=new ArrayList<>(); //list des objets à dessiner
		toDraw.add(game.getBackground());
		toDraw.add(game.getPlayer());
		
		//obstacles
		ArrayList<Entity> obs=game.getObstacles();

		//ajouts des obstacles à dessiner
		for(int i=0;i<obs.size();i++) {

			toDraw.add(obs.get(i));
		}

		return toDraw;
	}	

	//vérifie si le joueur est décédé
	public void checkEnd() {
		if (game.getPlayer().isChecked() && !debug) {
			this.game = new Game(sizeX,sizeY);//recommencement
		}
	}

	//défini le mode debug
	public void setDebug(boolean debug) {
		this.debug=debug;
		//fait "revivre" le joueur en revenant du mode debug si il a touché un obstacle
		this.game.player.setChecked(false);
	}

	//transforme les commandes reçu par la view et les transmets aux méthodes du modèle
	public void getCmd(char cmd) {

		if (cmd==' ') {
			game.commandSpace();
		}
	}

	//retourne le score
	public int getScore() {
		return game.getScore();
	}

	//appelé à chaque "frame"
	public void nextFrame(double now) {
		game.nextFrame(now-time);
	}
}