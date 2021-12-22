package com.example.lab_4_balls.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.lab_4_balls.R;

public class Score implements Drawable {
    private float x;
    private float y;
    private int score;
    private int color;

    public Score(Context context, float x, float y) {
        this.x = x;
        this.y = y;
        this.score = 0;
        this.color = ContextCompat.getColor(context, R.color.score);
    }

    public void draw(Canvas canvas) {
        String score = Integer.toString(this.score);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(75);
        canvas.drawText(score, x, y, paint);
    }

    public void inc() {
        this.score++;
    }

    public int getScore() {
        return score;
    }

    public void update() {

    }
}
