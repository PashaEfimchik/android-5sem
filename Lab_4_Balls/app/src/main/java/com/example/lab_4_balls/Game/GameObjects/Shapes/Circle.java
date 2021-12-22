package com.example.lab_4_balls.Game.GameObjects.Shapes;

import android.content.Context;
import android.graphics.Canvas;

import com.example.lab_4_balls.Game.Engine.Game;
import com.example.lab_4_balls.Point;

public class Circle extends Shape {
    protected float radius;

    public Circle(Context context, int colorId, float x, float y, float radius, float mass) {
        super(context, colorId, x, y, mass);
        this.radius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    @Override
    protected Point nearestPoint(float x, float y) {
        float dx = x - this.x;
        float dy = y - this.y;

        float len = (float) Math.sqrt(dx*dx + dy*dy);
        if (len < radius) {
            return new Point(x, y);
        }

        dx /= len;
        dy /= len;

        dx *= radius;
        dy *= radius;

        return new Point(this.x + dx, this.y + dy);
    }

    @Override
    public boolean collides(float x, float y) {
        float dx = Math.abs(x - this.x);
        float dy = Math.abs(y - this.y);

        float r1 = (float) Math.sqrt(dx*dx + dy*dy);
        float r2 = this.radius;
        if (r1 < r2) {
            return true;
        }
        return Math.abs(r1 - r2) < Game.ERR;
    }

    @Override
    public float surfaceAngle(Point point) {
        Point nearest = nearestPoint(point);
        return (float) Math.atan(-(nearest.x-x)/(nearest.y-y));
    }

    public float getRadius() {
        return radius;
    }
}