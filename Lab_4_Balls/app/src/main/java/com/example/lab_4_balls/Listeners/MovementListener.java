package com.example.lab_4_balls.Listeners;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class MovementListener implements View.OnTouchListener {
    private final int fieldWidth;
    private final float touchFieldPercentage = 0.5f;
    private float actuatorX = 0;
    private float velocityX = 0;
    private boolean isPressed;

    public MovementListener(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        fieldWidth = width;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isPressed(x)) {
                    setIsPressed(true);
                }
            case MotionEvent.ACTION_MOVE: {
                if (getIsPressed()) {
                    setActuatorX(x);
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                setIsPressed(false);
                resetActuator();
                return true;
            }
        }
        return v.onTouchEvent(event);
    }

    public boolean isPressed(float touchedX) {
        return (touchedX < fieldWidth *touchFieldPercentage) || (touchedX > fieldWidth *(1-touchFieldPercentage));
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuatorX(float touchedX) {
        if (touchedX < fieldWidth * touchFieldPercentage) {
            actuatorX = -1;
        } else if (touchedX > fieldWidth * (1 - touchFieldPercentage)) {
            actuatorX = 1;
        } else {
            actuatorX = 0;
        }
    }

    public float getActuatorX() {
        return actuatorX;
    }

    public void resetActuator() {
        actuatorX = 0;
    }
}