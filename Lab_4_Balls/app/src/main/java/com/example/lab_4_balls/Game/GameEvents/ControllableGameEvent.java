package com.example.lab_4_balls.Game.GameEvents;

public interface ControllableGameEvent {
    public int duration = 10_000;

    public void start();
    public void stop();
}
