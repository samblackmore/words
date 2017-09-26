package com.sam.words.components;

/**
 * Container for some text with dimensions
 */

class TextBox {
    private String text = "";
    private int width = 0;
    private int height = 0;
    private int textLeft = 0;
    private int textHeight = 0;
    private int textSize = 0;

    TextBox() {}

    TextBox(String text, int width, int textHeight, int textSize) {
        this.text = text;
        this.width = width;
        this.textHeight = textHeight;
        this.textSize = textSize;
    }

    @Override
    public String toString() {
        return text;
    }

    public int getWidth() {
        return width;
    }

    public int getTextHeight() {
        return textHeight;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getTextLeft() {
        return textLeft;
    }

    public void setTextLeft(int textLeft) {
        this.textLeft = textLeft;
    }

    public int getHeight() {
        return height;
    }

    public void addBottomPadding(int amount) {
        height = textHeight + amount;
    }
}
