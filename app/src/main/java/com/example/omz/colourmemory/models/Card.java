package com.example.omz.colourmemory.models;

/**
 * Created by Omz on 15/5/2017.
 */

public class Card {
    private boolean isRevealed;
    private boolean isMatched;
    private int imgRes;

    public Card() {
        isRevealed = false;
        isMatched = false;
        imgRes = 0;
    }

    public Card(int i) {
        isRevealed = false;
        isMatched = false;
        imgRes = i;
    }

    public boolean getIsRevealed() { return isRevealed; }
    public boolean getIsMatched() { return isMatched; }
    public int getImgRes() { return imgRes; }

    public void setIsRevealed(boolean r) { isRevealed = r; }
    public void setIsMatched(boolean r) { isMatched = r; }
    public void setImgRes(int i) { imgRes = i; }
}
