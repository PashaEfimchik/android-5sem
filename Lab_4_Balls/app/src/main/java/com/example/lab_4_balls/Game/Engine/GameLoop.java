package com.example.lab_4_balls.Game.Engine;

import android.graphics.Canvas;
import android.os.Build;
import android.view.SurfaceHolder;

import androidx.annotation.RequiresApi;

public class GameLoop extends Thread{
    public static final float MAX_UPS = 30;
    public static final float UPS_PERIOD = 1e3f/MAX_UPS;

    private boolean isRunning = false;

    private double averageUPS;
    private double averageFPS;

    private final SurfaceHolder surfaceHolder;
    private final Game game;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        Canvas canvas;
        startTime = System.currentTimeMillis();
        while (isRunning) {
            canvas = surfaceHolder.lockCanvas();
            try {
                game.update();
                updateCount++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (canvas != null) {
                    game.draw(canvas);
                    frameCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while(sleepTime < 0 && updateCount < MAX_UPS-1) {
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 1_000) {
                averageUPS = updateCount / (1e-3 * elapsedTime);
                averageFPS = frameCount / (1e-3 * elapsedTime);

                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }

    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    @Override
    public void interrupt() {
        isRunning = false;
        super.interrupt();
    }
}