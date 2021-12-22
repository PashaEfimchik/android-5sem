package com.example.lab_4_balls.Game.GameObjects;

import android.content.Context;

import com.example.lab_4_balls.Game.Engine.Game;
import com.example.lab_4_balls.Game.Engine.GameLoop;
import com.example.lab_4_balls.Game.GameObjects.Shapes.Rectangle;
import com.example.lab_4_balls.Listeners.MovementListener;
import com.example.lab_4_balls.Point;

public class Player extends Rectangle {
    public static final float SPEED_PIXELS_PER_SECOND = 700;
    public static float MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final MovementListener movementListener;

    public Player(Context context, MovementListener movementListener, int colorId, float x, float y, float width, float height, float mass) {
        super(context, colorId, x, y, width, height, mass);
        this.movementListener = movementListener;

    }

    @Override
    public void update() {
        float velocityX = movementListener.getActuatorX() * MAX_SPEED;
        velocity = new Point(velocityX, 0);
        if (x + velocityX + width/2 > Game.width || x + velocityX - width/2 < 0) {
            return;
        }
        x += velocity.x;
    }
}