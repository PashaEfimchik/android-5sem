package com.example.lab_4_balls.Game.Engine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.lab_4_balls.Activities.ScoresActivity;
import com.example.lab_4_balls.Game.Drawable;
import com.example.lab_4_balls.Game.GameEvents.GameEventsManager;
import com.example.lab_4_balls.Game.GameObjects.GameObject;
import com.example.lab_4_balls.Game.GameObjects.MovingCircle;
import com.example.lab_4_balls.Game.GameObjects.Player;
import com.example.lab_4_balls.Game.GameObjects.Shapes.Rectangle;
import com.example.lab_4_balls.Game.Updatable;
import com.example.lab_4_balls.Listeners.MovementListener;
import com.example.lab_4_balls.Point;
import com.example.lab_4_balls.R;
import com.example.lab_4_balls.Game.Score;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends SurfaceView implements SurfaceHolder.Callback, Drawable, Updatable {
    public static final float ERR = 1;

    private final GameLoop gameLoop;
    public final Player player;
    private final MovementListener movementListener;
    private final Score score;
    private final GameEventsManager gameEventsManager;

    private final int numCores;

    public static int width;
    public static int height;

    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    public ArrayList<Drawable> drawables = new ArrayList<>();
    public ArrayList<Updatable> updatables = new ArrayList<>();

    public static ArrayList<Point> points = new ArrayList<>();
    public static Context context;

    public static final Object locker = new Object();
    public static final ReentrantLock rlocker = new ReentrantLock();

    public Game(Context context) {
        super(context);
        Game.context = context;

        numCores = Runtime.getRuntime().availableProcessors();;

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        movementListener = new MovementListener(context);
        setOnTouchListener(movementListener);
        player = new Player(context, movementListener, R.color.player, width/2f, height-150, 250, 25, 0) {
            @Override
            public void onCollision(GameObject sender, GameObject collisionWith) {
                super.onCollision(sender, collisionWith);
                score.inc();
            }
        };
        gameObjects.add(player);
        drawables.add(player);
        updatables.add(player);

        int boundColor = R.color.border;
        float woh = 20;
        Rectangle topBound = new Rectangle(context, boundColor, width/2f, 1, (float) width, woh, 0);
        Rectangle leftBound = new Rectangle(context, boundColor, 1, height/2f, woh, (float) height, 0);
        Rectangle rightBound = new Rectangle(context, boundColor, width-1, height/2f, woh, (float) height, 0);
        Rectangle bottomBound = new Rectangle(context, boundColor, width/2f, (float) height-1, (float) width, woh, 0) {
            @Override
            public void onCollision(GameObject sender, GameObject collisionWith) {
                gameOver();
            }
        };

        gameObjects.add(topBound);
        gameObjects.add(leftBound);
        gameObjects.add(rightBound);
        gameObjects.add(bottomBound);

        drawables.add(topBound);
        drawables.add(leftBound);
        drawables.add(rightBound);
        drawables.add(bottomBound);

        GameObject o = MovingCircle.withRandChars(context, width, height, 50, 1);
        gameObjects.add(o);
        drawables.add(o);
        updatables.add(o);

        score = new Score(context, 25, 100);
        drawables.add(score);

        gameLoop = new GameLoop(this, surfaceHolder);

        gameEventsManager = new GameEventsManager(this, width-100, 100);
        drawables.add(gameEventsManager);
        updatables.add(gameEventsManager);

        setFocusable(true);
    }

    @NonNull
    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        synchronized (locker) {
            rlocker.lock();
            super.draw(canvas);

            Game.canvas = canvas;

            for (Drawable drawable : drawables) {
                drawable.draw(canvas);
            }

            for (Point point: points) {
                point.draw(canvas);
            }

            points.clear();
            rlocker.unlock();
        }
    }

    public static Canvas canvas;
    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.red);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.red);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update() {
        synchronized (locker) {
            rlocker.lock();
            for (Updatable updatable : updatables) {
                updatable.update();
            }

            for (int i = 0; i < gameObjects.size(); i++) {
                gameObjects.subList(i + 1, gameObjects.size()).parallelStream().forEach(gameObjects.get(i)::collide);
            }
            rlocker.unlock();
        }
    }

    public static final String SCORE_EXTRA = "com.example.lab_4_balls.SCORE";
    public void gameOver() {
        gameLoop.interrupt();
        Intent intent = new Intent(context, ScoresActivity.class);
        intent.putExtra(SCORE_EXTRA, score.getScore());
        context.startActivity(intent);
        ScoresActivity.score = score.getScore();
    }

    public void addObject(Object o) {
        synchronized (locker) {
            rlocker.lock();
            GameObject go = (GameObject) o;
            if (go != null) {
                gameObjects.add(go);
            }
            Drawable d = (Drawable) o;
            if (d != null) {
                drawables.add(d);
            }
            Updatable u = (Updatable) o;
            if (u != null) {
                updatables.add(u);
            }
            rlocker.unlock();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void removeObject(Object o) {
        synchronized (locker) {
            rlocker.lock();
            GameObject go = (GameObject) o;
            if (go != null) {
                gameObjects.removeIf(i -> i == go);
            }
            Drawable d = (Drawable) o;
            if (d != null) {
                gameObjects.removeIf(i -> i == d);
            }
            Updatable u = (Updatable) o;
            if (u != null) {
                updatables.removeIf(i -> i == u);
            }
            rlocker.unlock();
        }
    }
}