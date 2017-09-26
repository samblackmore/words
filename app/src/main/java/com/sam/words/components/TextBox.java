package com.sam.words.components;

/**
 * Container for some text with dimensions
 */

class TextBox {
    private String text;
    private int width;
    private int height;
    private int textSize;

    TextBox(String text, int width, int height, int textSize) {
        this.text = text;
        this.width = width;
        this.height = height;
        this.textSize = textSize;
    }

    @Override
    public String toString() {
        return text;
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
