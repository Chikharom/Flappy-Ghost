package com.example.flappyghost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

class GameView extends SurfaceView implements SurfaceHolder.Callback{

    GameThread gameRun;
    int sizeX;
    int sizeY;
    Bitmap[] obstacles=new Bitmap[28];
    Bitmap[] entity=new Bitmap[3];
    Bitmap drawAct;
    Controler c1;
    double[] ratio=new double[2];
    double currentTime;
    double lastTime=System.nanoTime();
    boolean pauseBool=false;
    boolean debug=false;
    TextView scoreBox;

    public GameView(Context cont) {
        super(cont);
        init(cont);
    }

    public void setDebug(boolean toggle){
        debug=toggle;
        c1.setDebug(toggle);
    }

    public void pause(){
        if (pauseBool){
            pauseBool=false;
        }else{
            pauseBool=true;
        }
    }

    public void getBitmaps(){

        entity[0]=BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        entity[1]=BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
        obstacles[27]=BitmapFactory.decodeResource(getResources(), R.drawable.palmier);

        for (int i=0; i<27; i++){
            int id=getResources().getIdentifier("f"+i,"drawable", "com.example.flappyghost");
           obstacles[i]=BitmapFactory.decodeResource(getResources(), id);
        }
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void startGame(){
        gameRun.setRunning(true);
        gameRun.start();
    }

    public void init(Context con){
        getHolder().addCallback(this);
        gameRun=new GameThread(this, getHolder());
        setFocusable(true);
        getBitmaps();


    }

    public void addTextBox(TextView tw){
        scoreBox=tw;
    }

    public void update(double now, Canvas canvas){

        if (c1==null){
            sizeX=canvas.getWidth();
            sizeY=canvas.getHeight();
            ratio[1]=(double)sizeY/400.0;
            ratio[0]=ratio[1];
            c1=new Controler((int)(sizeX/ratio[1]), 400);
            //c1.setDebug(true)

        }

        currentTime=now*1e-9;
        if (!pauseBool) {
            c1.nextFrame(currentTime);
            c1.checkEnd();
        }
        c1.setTime(currentTime);

    }

    @Override
    public void draw(Canvas canvas){

        if (canvas!=null && c1!=null) {

            ArrayList<Entity> toDraw=c1.getObjects();
            super.draw(canvas);
            canvas.drawColor(Color.WHITE); //clearRect
            Paint paint = new Paint(); //setFill

            Rect rect;

            Entity bg=toDraw.get(0);
            double[] posBG=bg.getPos();

            drawAct=entity[0];
            rect = new Rect((int)(posBG[0]*ratio[0]), (int)(posBG[1]*ratio[1]), (int)((posBG[0]*ratio[0])+sizeX+10), (int)((posBG[1]*ratio[1])+sizeY));
            canvas.drawBitmap(drawAct, null, rect, paint);
            rect= new Rect((int)(posBG[0]*ratio[0])+sizeX-10, (int)(posBG[1]*ratio[1]), (int)((posBG[0]*ratio[0])+2*sizeX), (int)((posBG[1]*ratio[1])+sizeY));
            canvas.drawBitmap(drawAct, null, rect, paint);



            for (int i=1; i<toDraw.size(); i++){

                Entity obj=toDraw.get(i);
                double[] pos={obj.getPos()[0], obj.getPos()[1]};
                    if (i == 1) {
                        continue;
                    }

                    if (obj.getImage().length()>3){

                        drawAct=obstacles[27];
                        paint.setColor(Color.YELLOW);

                    }else{

                        paint.setColor(Color.YELLOW);
                        int num=Integer.parseInt(obj.getImage());
                        drawAct=obstacles[num];
                    }

                    rect=new Rect((int)(pos[0]*ratio[0]), (int)(pos[1]*ratio[1]),
                            (int)((pos[0]+obj.getRadius()*2)*ratio[0]), (int)((pos[1]+obj.getRadius()*2)*ratio[1]));

                    if (debug) {

                        boolean colision=((Ghost)toDraw.get(1)).detectHit(obj);

                        if (colision) {
                            paint.setColor(Color.RED);
                        }
                        pos=obj.getPosC();
                        canvas.drawCircle((int)(pos[0] * ratio[0]), (int)(pos[1] * ratio[1]), (int)(obj.getRadius()  * ratio[1]), paint);
                    }else{
                        canvas.drawBitmap(drawAct, null, rect, null);
                    }


            }

            paint.setColor(Color.BLACK);
            paint.setTextSize(200);
            canvas.drawText(""+c1.getScore(), 100, 200, paint);
            Entity ghost=toDraw.get(1);
            double[] posG=ghost.getPosC();

            if (debug){

                canvas.drawCircle((int)(posG[0]*ratio[0]),(int)(posG[1]*ratio[1]), (int)(ghost.getRadius()*ratio[1]),paint);
            }else{

                posG=ghost.getPos();
                rect=new Rect((int)(posG[0]*ratio[0]), (int)(posG[1]*ratio[1]),
                        (int)((posG[0]+ghost.getRadius()*2)*ratio[0]), (int)((posG[1]+ghost.getRadius()*2)*ratio[0]));
                canvas.drawBitmap(entity[1], null, rect, null);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        c1.getCmd(' ');
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            Thread.sleep(100);
        }catch(Exception e){

        }
        startGame();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
