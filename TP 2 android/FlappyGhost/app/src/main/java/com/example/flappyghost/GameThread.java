package com.example.flappyghost;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class GameThread extends Thread{

    SurfaceHolder sHolder;
    GameView gameView;
    boolean running=false;
    public static Canvas canvas;
    public double lastT=System.nanoTime();
    public double now;

    public GameThread(GameView g, SurfaceHolder sH){
        super();
        gameView=g;
        sHolder=sH;
    }

    public void setRunning(boolean toggle){
        running=toggle;
    }

    @Override
    public void run() {

        while (running) {

            now = System.nanoTime();
            double dt = (now - lastT)*1e-6;

            if (dt<25) {

                try {
                    Thread.sleep((int)(30-dt));
                    now = System.nanoTime();
                    dt = (now - lastT)*1e-6;

                }catch(Exception e){}
            }

            lastT=now;
            canvas = null;

            try {

                canvas = sHolder.lockCanvas();
                synchronized (sHolder) {
                    this.gameView.update(System.nanoTime(), canvas);
                    this.gameView.draw(canvas);
                }
            } catch (Exception e) {
                //nada
            } finally {

                if (canvas != null) {
                    try {
                        sHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

