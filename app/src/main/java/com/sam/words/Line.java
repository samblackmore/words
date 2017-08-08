package com.sam.words;

import android.graphics.Typeface;

/**
 * A line on a page
 */

public class Line {
    private int x;
    private int y;
    private int textSize;
    private Typeface typeface;
    private String text;

    public Line(int x, int y, int textSize, Typeface typeface, String text) {
        this.x = x;
        this.y = y;
        this.textSize = textSize;
        this.typeface = typeface;
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTextSize() {
        return textSize;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public String getText() {
        return text;
    }
}
