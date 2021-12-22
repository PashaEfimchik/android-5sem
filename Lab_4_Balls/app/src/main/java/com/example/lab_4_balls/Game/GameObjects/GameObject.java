package com.example.lab_4_balls.Game.GameObjects;

import android.graphics.Canvas;

import com.example.lab_4_balls.Game.Drawable;
import com.example.lab_4_balls.Game.Engine.Game;
import com.example.lab_4_balls.Game.Engine.GameLoop;
import com.example.lab_4_balls.Game.Updatable;
import com.example.lab_4_balls.Point;
import com.example.lab_4_balls.Utils;

public abstract class GameObject implements Drawable, Updatable {
    public static final float SPEED_PIXELS_PER_SECOND = 50;
    public static final float MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    private static final Object locker = new Object();
    protected float mass;
    protected float x;
    protected float y;
    protected Point velocity;

    public GameObject(float x, float y, float mass) {
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.velocity = new Point(0, 0);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public void setVelocity(float x, float y) {
        this.velocity = new Point(x, y);
    }

    public Point getVelocity() {
        return velocity;
    }

    public Point coordinates() {
        return new Point(x, y);
    }

    public int moved = 0;

    public static final float ERR_MUL = 10;

    public void randomMove() {
        x = Utils.rand(100, Game.width - 100);
        y = Utils.rand(100, Game.height/2f - 100);
    }

    @Override
    public void update() {
        x += velocity.x;
        y += velocity.y;
        if (
                0 + Game.ERR * ERR_MUL> x ||
                        x > Game.width - Game.ERR * ERR_MUL ||
                        0 + Game.ERR * ERR_MUL > y ||
                        y > Game.height - Game.ERR * ERR_MUL) {
            while (0 > x) {
                x += MAX_SPEED;
                moved++;
                if (moved > 100) {
                    randomMove();
                    moved = 0;
                    return;
                }
            }
            while (x > Game.width) {
                x -= MAX_SPEED;
                moved++;
                if (moved > 100) {
                    randomMove();
                    moved = 0;
                    return;
                }
            }
            while (0 > y) {
                y += MAX_SPEED;
                moved++;
                if (moved > 100) {
                    randomMove();
                    moved = 0;
                    return;
                }
            }
            while (y > Game.height) {
                y -= MAX_SPEED;
                moved++;
                if (moved > 100) {
                    randomMove();
                    moved = 0;
                    return;
                }
            }
            moved = 0;
        }
        if (velocity.getLen() > MAX_SPEED) {
            velocity.setLen(MAX_SPEED);
        }
    }

    protected abstract Point nearestPoint(float x, float y);
    protected Point nearestPoint(Point point) {
        return nearestPoint(point.x, point.y);
    }
    protected Point nearestPoint(GameObject gameObject){
        return nearestPoint(gameObject.x, gameObject.y);
    }
    protected abstract boolean collides(float x, float y);
    protected boolean collides(Point point) {
        return collides(point.x, point.y);
    }

    public boolean collides(GameObject gameObject) {
        Point nearest = gameObject.nearestPoint(this.x, this.y);
        return collides(nearest);
    }

    public abstract float surfaceAngle(Point point);

    public void collide(GameObject gameObject) {
        if (mass == 0 && gameObject.mass == 0) {
            return;
        }

        Point p1 = this.nearestPoint(gameObject);
        Point p2 = gameObject.nearestPoint(p1);

        boolean c1 = collides(p2);
        boolean c2 = gameObject.collides(p1);

        if (c1 || c2)
        {
            Game.points.add(p1);
            Game.points.add(p2);

            synchronized (locker) {

                onCollision(this, gameObject);
                gameObject.onCollision(gameObject, this);

                float vx1 = velocity.x;
                float vy1 = velocity.y;

                float vx2 = gameObject.velocity.x;
                float vy2 = gameObject.velocity.y;

                float vl1 = (float) Math.sqrt(vx1 * vx1 + vy1 * vy1);
                float vl2 = (float) Math.sqrt(vx2 * vx2 + vy2 * vy2);

                Point v1 = velocity;
                Point v2 = gameObject.velocity;

                float m1 = this.mass;
                float m2 = gameObject.mass;

                float theta1 = velocity.angle();
                float theta2 = gameObject.velocity.angle();

                Point x1 = coordinates();
                Point x2 = gameObject.coordinates();

                float rx1 = vx1, ry1 = vy1, rx2 = vx2, ry2 = vy2;
                float phi = (float) (Math.abs(surfaceAngle(p1)));

                if (m1 == 0) {
                    phi = (float) (Math.abs(surfaceAngle(p1)));

                    if (Math.abs(phi) < Game.ERR) {
                        ry2 = -vy2;
                    }
                    else if (Math.abs(Math.abs(phi) - Math.PI/2) < Game.ERR) {
                        rx2 = - vx2;
                    }

                    float dpi = 0;

                } else if (m2 == 0) {
                    phi = surfaceAngle(p2);
                    if (Math.abs(phi) < Game.ERR) {
                        ry1 = -vy1;
                    }
                    else if (Math.abs(Math.abs(phi) - Math.PI/2) < Game.ERR) {
                        rx1 = -vx1;
                    }
                } else {
                    double a1 = vl1*Math.cos(theta1-phi)*(m1-m2)+2*m2*vl2*Math.cos(theta2-phi);

                    rx1 = (float)((a1 * Math.cos(phi) + vl1 * Math.sin(theta1-phi) * Math.cos(phi+Math.PI/2)) / (m1 + m2));

                    ry1 = (float)((a1 * Math.sin(phi) + vl1 * Math.sin(theta1-phi) * Math.sin(phi+Math.PI/2)) / (m1 + m2));

                    double a2 = vl2 * Math.cos(theta2-phi) * (m2-m1) + 2 * m1 * vl1 * Math.cos(theta1-phi);
                    rx2 = (float)((a2 * Math.cos(phi) + vl2*Math.sin(theta2-phi)*Math.cos(phi+Math.PI/2)) / (m1+m2));
                    ry2 = (float)((a2 * Math.sin(phi) + vl2*Math.sin(theta2-phi)*Math.sin(phi+Math.PI/2)) / (m1+m2));
                }

                Point rv1 = new Point(rx1, ry1);
                Point rv2 = new Point(rx2, ry2);

                if (rv1.getLen() > this.MAX_SPEED) {
                    rv1.setLen(this.MAX_SPEED);
                }
                if (rv2.getLen() > gameObject.MAX_SPEED) {
                    rv2.setLen(gameObject.MAX_SPEED);
                }

                this.velocity = rv1;
                gameObject.velocity = rv2;

                while (collides(gameObject) || gameObject.collides(this)) {
                    if (m2 != 0) {
                        gameObject.update();
                    }
                }
            }
        }
    }

    public void onCollision(GameObject sender, GameObject collisionWith) {

    }

    public float getVelocityX() {
        return velocity.x;
    }

    public float getVelocityY() {
        return velocity.y;
    }

    public void setVelocityX(float x) {
        velocity = new Point(x, this.y);
    }

    public void setVelocityY(float y) {
        velocity = new Point(this.x, y);
    }
}