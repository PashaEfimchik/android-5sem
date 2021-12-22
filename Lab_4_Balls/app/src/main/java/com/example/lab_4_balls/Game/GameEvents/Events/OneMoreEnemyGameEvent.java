package com.example.lab_4_balls.Game.GameEvents.Events;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.lab_4_balls.Game.Engine.Game;
import com.example.lab_4_balls.Game.GameObjects.GameObject;
import com.example.lab_4_balls.Game.GameObjects.MovingCircle;
import com.example.lab_4_balls.Game.GameEvents.GameEvent;

import java.util.Calendar;

public class OneMoreEnemyGameEvent extends GameEvent {
    //GameObject spawnedEnemy;

    public OneMoreEnemyGameEvent(Game game) {
        super(game);
        //spawnedEnemy = MovingCircle.withRandChars(game.getContext(), game.getWidth(), game.getHeight());
    }

    @Override
    public void start() {
        super.start();

        /*for (int i = 0; i < game.gameObjects.size(); i++) {
            if (game.gameObjects.get(i).collides(spawnedEnemy)) {
                spawnedEnemy.randomMove();
                i = -1;
            }
        }
        game.addObject(spawnedEnemy);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void stop() {
//        game.removeObject(spawnedEnemy);
    }

    @Override
    public void update() {

    }
}