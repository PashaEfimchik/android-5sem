package com.example.lab_4_balls.Game.GameEvents;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.lab_4_balls.Game.Drawable;
import com.example.lab_4_balls.Game.Engine.Game;
import com.example.lab_4_balls.Game.GameEvents.Events.OneMoreEnemyGameEvent;
import com.example.lab_4_balls.Game.GameEvents.Events.PlayerSlowedMovementGameEvent;
import com.example.lab_4_balls.Game.Updatable;
import com.example.lab_4_balls.R;
import com.example.lab_4_balls.Utils;

import java.util.ArrayList;
import java.util.Calendar;

public class GameEventsManager implements Drawable, Updatable {
    public static int gameEventPeriod_ms = 15_000;

    private long lastEvent = 0;
    private Game game = null;
    private int color;

    public ArrayList<GameEvent> events = new ArrayList<>();
    public ArrayList<Object> eventTypes = new ArrayList<>();

    private int x;
    private int y;

    public GameEventsManager(Game game, int x, int y) {
        this.game = game;
        lastEvent = Calendar.getInstance().getTimeInMillis();

        this.x = x;
        this.y = y;
        this.color = ContextCompat.getColor(Game.context, R.color.gameEventTimer);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(75);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update() {
        long currTime = Calendar.getInstance().getTimeInMillis();
        if (Math.abs(currTime - lastEvent) >= gameEventPeriod_ms) {
            createEvent();
            lastEvent = currTime;
        }

        GameEvent toRemove = null;
        for (GameEvent event: events) {
            if (currTime - event.startTime > event.duration) {
                event.stop();
                toRemove = event;
            }
            else {
                event.update();
            }
        }

        events.remove(toRemove);
    }

    public void createEvent() {
        GameEvent gameEvent;
        float r = Utils.rand(0, 1);
        if (r < 0.5f) {
            gameEvent = new OneMoreEnemyGameEvent(game);
        }
        else {
            gameEvent = new PlayerSlowedMovementGameEvent(game);
        }

        events.add(gameEvent);
        gameEvent.start();
    }
}