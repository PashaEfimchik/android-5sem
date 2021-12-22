package com.example.lab_4_balls;

import com.example.lab_4_balls.Models.UserScore;

import java.util.ArrayList;

public class Utils {
    public static float rand(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }
}