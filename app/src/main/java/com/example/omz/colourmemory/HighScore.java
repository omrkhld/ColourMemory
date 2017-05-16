package com.example.omz.colourmemory;

import io.realm.RealmObject;

/**
 * Created by Omz on 15/5/2017.
 */

public class HighScore extends RealmObject {

    int rank, score;
    String name;
}
