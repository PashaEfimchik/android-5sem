package com.example.lab_4_balls.Game.GameEvents.Events;

import com.example.lab_4_balls.Game.Engine.Game;
import com.example.lab_4_balls.Game.GameObjects.Player;
import com.example.lab_4_balls.Game.Updatable;
import com.example.lab_4_balls.Point;
import com.example.lab_4_balls.Game.GameEvents.GameEvent;

public class PlayerSlowedMovementGameEvent extends GameEvent implements Updatable {
    private boolean isActive = false;

    public PlayerSlowedMovementGameEvent(Game game) {
        super(game);
    }

    @Override
    public void start() {
        super.start();
        isActive = true;
        Player.MAX_SPEED /= 2;
    }

    @Override
    public void stop() {
        isActive = false;
        Player.MAX_SPEED *= 2;
    }

    @Override
    public void update() {
    }
}