package com.example.lab_4_balls.Models;

import androidx.annotation.NonNull;

public class UserScore {
    public int id = -1;
    public String username = null;
    public int score = 0;

    public UserScore(int id, String username, int score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public UserScore(String username, int score) {
        this.username = username;
        this.score = score;
    }

    @Override
    public String toString() {
        return "UserScore{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", score=" + score +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int compareTo(UserScore o) {
        return Math.max(score, o.score);
    }
}