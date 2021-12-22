package com.example.lab_4_balls.Game.GameObjects.Shapes;

import android.content.Context;
import android.graphics.Canvas;

import com.example.lab_4_balls.Game.Engine.Game;
import com.example.lab_4_balls.Point;

public class Rectangle extends Shape {
    protected final float width;
    protected final float height;

    public Rectangle(Context context, int colorId, float x, float y, float width, float height, float mass) {
        super(context, colorId, x, y, mass);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(
                x - width/2,
                y + height/2,
                x + width/2,
                y - height/2,
                paint
        );
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    @Override
    protected Point nearestPoint(float x, float y) {
        float vX = this.x-width/2;
        float vY = this.y-height/2;

        // to new coordinates system with center (this.x - width/2, this.y - height/2)
        float newX = x - vX;
        float newY = y - vY;

        if (0 - Game.ERR < newX && newX < width + Game.ERR) {
            if (newY < 0 + Game.ERR) {
                return new Point(x, this.y - height/2);
            }
            else if (newY > height - Game.ERR) {
                return new Point(x, this.y + height/2);
            }
            else if (0 - Game.ERR < newY && newY < height + Game.ERR) {
                return new Point(x, y);
            }
        }
        if (0 - Game.ERR < newY && newY < height + Game.ERR) {
            if (newX < 0) {
                return new Point(this.x - width/2, y);
            }
            if (newX > width - Game.ERR) {
                return new Point(this.x + width/2, y);
            }
            if (0 - Game.ERR < newX && newX < width + Game.ERR) {
                return new Point(x, y);
            }
        }
        if (newX < 0 + Game.ERR && newY < 0 + Game.ERR) {
            return new Point(this.x - width/2, this.y - height/2);
        }
        if (newX > 0 - Game.ERR && newY < 0 + Game.ERR) {
            return new Point(this.x + width/2, this.y - height/2);
        }
        if (newX > width - Game.ERR && newY > height - Game.ERR) {
            return new Point(this.x + width/2, this.y + height/2);
        }
        if (newX < 0 + Game.ERR && newY > height - Game.ERR) {
            return new Point(this.x - width/2, this.y + height/2);
        }

        return new Point(x, y);
    }

    @Override
    protected boolean collides(float x, float y) {
        float vX = this.x-width/2;
        float vY = this.y-height/2;

        x -= vX;
        y -= vY;

        if (0 -Game.ERR < x && x < width + Game.ERR && 0 - Game.ERR < y && y < height + Game.ERR) {
            return true;
        }

        boolean onHorSide = false;
        if (Math.abs(x) < Game.ERR) {
            onHorSide = true;
        }
        else if (Math.abs(width - x) < Game.ERR) {
            onHorSide = true;
        }

        boolean onVertSide = false;
        if (Math.abs(y) < Game.ERR) {
            onVertSide = true;
        }
        else if (Math.abs(height - y) < Game.ERR) {
            onVertSide = true;
        }

        if (onHorSide && onVertSide) {
            return true;
        }

        if (onHorSide && 0 < y && y < height) {
            return true;
        }
        if (onVertSide && 0 < x && x < width) {
            return true;
        }

        return false;
    }

    @Override
    public float surfaceAngle(Point point) {
        Point n = nearestPoint(point);

        // to coordinates center
        float vX = this.x-width/2;
        float vY = this.y-height/2;

        float x = n.x-vX;
        float y = n.y-vY;

        if (
                0 < y && y < height &&
                        0 < x && x < width
        ) {
            if (height > width) {
                return (float) Math.PI/2;
            }
            else {
                return 0;
            }
        }
        if (0 < y && y < height) {
            return (float) Math.PI/2;
        }
        if (0 < x && x < width) {
            return (float) 0;
        }
        if (Math.abs(x) < Game.ERR) {
            if (Math.abs(y) < Game.ERR) {
                return (float) -Math.PI/2;
            }
            if (Math.abs(y-height) < Game.ERR) {
                return (float) Math.PI/2;
            }

        }
        if (Math.abs(x-width) < Game.ERR) {
            if (Math.abs(y) < Game.ERR) {
                return (float) Math.PI/2;
            }
            if (Math.abs(y-height) < Game.ERR) {
                return (float) -Math.PI/2;
            }
        }

        return 0;
    }
}