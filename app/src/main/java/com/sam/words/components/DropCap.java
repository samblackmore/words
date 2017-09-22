package com.sam.words.components;

/**
 * The big first letter at the beginning of a chapter
 */

public class DropCap {
    private char dropCap;
    private int width;
    private int height;
    private int textSize;

    public DropCap(char dropCap, int width, int height, int textSize) {
        this.dropCap = dropCap;
        this.width = width;
        this.height = height;
        this.textSize = textSize;
    }

    @Override
    public String toString() {
        return String.valueOf(dropCap);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextSize() {
        return textSize;
    }
}
