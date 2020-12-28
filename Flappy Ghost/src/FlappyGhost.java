import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*Raphael Tihon
 * Omar Chikhar
 * 22/4/2018
 * Classe principale du jeu
 * */
public class FlappyGhost extends Application{

	boolean running=true; //boolean qui d�termine si le jeu fonctionne
	static double[] dim; //dimmensions du canvas
	static Controler c1; //controleur
	String twadoTest=""; //variable pour la sequence de texte entr�e
	static boolean flipped=false; //variable controlant l'inversion du canvas

	public void start(Stage stagerino ) { //m�thode de lancement

		/* Lorsqu�on clique ailleurs sur la sc�ne, le focus retourne sur le canvas */ 
		//stagerino.setMaximized(true);
		VBox container1=new VBox(); //conteeur principal
		
		Scene scene=new Scene(container1);
		stagerino.setScene(scene);
		
		stagerino.setWidth(640); 
		stagerino.setHeight(475); //400 la taille du canvas, 440 l'int�rieur de la fen�tre
								//+35 la bordure de la fen�tre
		stagerino.setResizable(false);
		stagerino.show();

		//scene contenant le canvas et les boutons

		Canvas can=new Canvas(stagerino.getWidth(), stagerino.getHeight()-40);
		//d�finition des dimensions du canvas
		double[] d={can.getWidth(), can.getHeight()};
		dim=d;
		c1=new Controler(640, 400);
		
		can.setRotationAxis(new Point3D(1,0,0));//d�finition de l'axe de rotation du canvas pour le code secret

		HBox container2=new HBox();
		container2.setAlignment(Pos.CENTER);//centre les �l�ments de la hbox

		Platform.runLater(() -> { can.requestFocus(); });//set le focus sur le canvas
		
		scene.setOnMouseClicked((event) -> { can.requestFocus(); }); 


		container1.getChildren().add(can);
		container1.getChildren().add(container2);

		//cr�ation du bouton pause
		Button pause=new Button("Pause");

		//cr�ation de la textBox
		Text score=new Text("Score: 0");

		
		pause.setOnAction((event->{
			running=!running;
			c1.setTime(System.nanoTime()*1e-9);//red�fini le temps apr�s une pause

		}));

		//Checkbox du mode debug
		CheckBox cb=new CheckBox("Mode Debug");

		cb.setOnAction((event->{

			c1.setDebug(cb.isSelected());

		}));

		//�v�nement lorsque une touche est enfonc�
		scene.setOnKeyTyped((event->{

			//r�cup�re le symbole de la touche
			char cmd=event.getCharacter().charAt(0);

			if ((int)cmd==27) {
				Platform.exit();
			}

			if (twadoTest.length()<5) { 

				twadoTest+=cmd;

			}else {
				//ajoute le nouveau caract�re et enl�ve le plus ancien
				twadoTest=twadoTest.substring(1)+cmd;
			}

			if (twadoTest.equals("twado")) {
				flipped=true;
				can.setRotate(can.getRotate()+180);
			}else {
				flipped=false;
			}

			c1.getCmd(cmd); //appelle la m�thode du contr�leur assignant la commande

		}));


		//timer de l'animation principale
		AnimationTimer timmy=new AnimationTimer() {			

			@Override
			public void handle(long now) {

				if (running) { //si le jeu est en cours

					
					double currentTime = now*1e-9;
					c1.nextFrame(currentTime);
					c1.checkEnd();
					c1.setTime(currentTime);
				}
				
				//dessine les �l�ments
				draw(can, c1.getObjects(), cb.isSelected());
				score.setText(""+c1.getScore());
			}
		};

		timmy.start();

		//ajout du "menu" en dessous du canvas
		container2.getChildren().add(pause);
		container2.getChildren().add(new Separator());
		container2.getChildren().add(cb);
		container2.getChildren().add(new Separator());
		container2.getChildren().add(score);

	}

	//m�thode qui dessine les �l�ments
	public static void draw(Canvas can, ArrayList<Entity> toDraw, boolean debug) {

		int[] cDim=c1.getDim();//dimensions du canvas 

		//definition du ratio entre le canvas et le controleur
		double ratioX=dim[0]/cDim[0];
		double ratioY=dim[1]/cDim[1];

		GraphicsContext g=can.getGraphicsContext2D();
		g.clearRect(0, 0, can.getWidth(), can.getHeight());

		//le background est dessin� 2 fois, l'un d�rri�re l'autre
		Entity bg=toDraw.get(0);
		g.drawImage(bg.getImage(), bg.getPos()[0]*ratioX, bg.getPos()[1]*ratioY, 
				can.getWidth(), can.getHeight());
		g.drawImage(bg.getImage(), bg.getPos()[0]*ratioX+can.getWidth(),
				bg.getPos()[1]*ratioY, can.getWidth(), can.getHeight());

		if (!debug) {

			//dessine le fant�me 
			Entity ghost=toDraw.get(1);
			g.drawImage(ghost.getImage(), ghost.getPos()[0]*ratioX,
					ghost.getPos()[1]*ratioY, 2*ghost.getRadius()*ratioX, 2*ghost.getRadius()*ratioY);
		}else {

			//dessine le hitcircle du fant�me
			g.setFill(Color.rgb(0, 0, 0));
			Entity ghost=toDraw.get(1);
			g.fillOval(ghost.getPos()[0]*ratioX, ghost.getPos()[1]*ratioY,
					2*ghost.getRadius()*ratioX, 2*ghost.getRadius()*ratioY);
		}

		//boucle de dessin des obstacles
		for (int i=2; i<toDraw.size(); i++) {

			Entity obj=toDraw.get(i);

			if (!debug) {

				//dessin des obstacles
				g.drawImage(obj.getImage(), obj.getPos()[0]*ratioX, obj.getPos()[1]*ratioY,
						2*obj.getRadius()*ratioX, 2*obj.getRadius()*ratioY);
			}else {
				//v�rifie si le hitcircle doit �tre rouge
				boolean colision=((Ghost)toDraw.get(1)).detectHit(obj);

				if (colision) {

					//dessine l'obstacle en rouge
					g.setFill(Color.RED);
					g.fillOval(obj.getPos()[0]*ratioX, obj.getPos()[1]*ratioY, 
							2*obj.getRadius()*ratioX, 2*obj.getRadius()*ratioY);

				}else {

					//dessine l'obstacle en jaune
					g.setFill(Color.YELLOW);
					g.fillOval(obj.getPos()[0]*ratioX, obj.getPos()[1]*ratioY, 
							2*obj.getRadius()*ratioX, 2*obj.getRadius()*ratioY);
				}
			}
		}
	}

	public static void main(String[] args) {
		FlappyGhost.launch();
	}

}
