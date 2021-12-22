package com.example.lab_4_balls.Game.GameObjects.Shapes;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.lab_4_balls.Game.GameObjects.GameObject;

public abstract class Shape extends GameObject {
    protected final Paint paint;

    public Shape(Context context, int colorId, float x, float y, float mass) {
        super(x, y, mass);
        paint = new Paint();
        int color = ContextCompat.getColor(context, colorId);
        paint.setColor(color);
    }
}
