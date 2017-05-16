package com.example.omz.colourmemory;

import android.support.annotation.NonNull;

import io.realm.RealmObject;

/**
 * Created by Omz on 15/5/2017.
 */

public class Highscore extends RealmObject {

    int score;
    String name;

    public Highscore() {
    }

    public int getScore() { return score; }
    public String getName() { return name; }

    public void setScore(int s) { score = s; }
    public void setName(String n) { name = n; }
}
