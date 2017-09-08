package com.sam.words;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.List;

/**
 * A page in a story. The first page in a chapter has a chapter title, a drop cap, and lines around
 * the drop cap. The remaining pages have only lines.
 */

class Page {
    private Typeface typeface;
    private int textSize;
    private int lineHeight;
    private float lineSeparation;
    private String chapterTitle;
    private DropCap dropCap;
    private List<String> dropCapLines;
    private List<String> lines;

    Page(Typeface typeface, int textSize, int lineHeight, float lineSeparation) {
        this.typeface = typeface;
        this.textSize = textSize;
        this.lineHeight = lineHeight;
        this.lineSeparation = lineSeparation;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public void setDropCap(DropCap dropCap) {
        this.dropCap = dropCap;
    }

    public void setDropCapLines(List<String> dropCapLines) {
        this.dropCapLines = dropCapLines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    void draw(Canvas canvas, Paint paint) {
        int lineSpacing = (int) ((lineHeight * lineSeparation) - lineHeight);

        paint.setTypeface(typeface);

        if (dropCap != null) {
            paint.setTextSize(dropCap.getTextSize());
            canvas.drawText(dropCap.toString(), 0, dropCap.getHeight(), paint);

            if (dropCapLines != null) {
                paint.setTextSize(textSize);
                int dropCapLinesX = dropCap.getWidth() + lineSpacing;

                for (int i = 1; i < dropCapLines.size() + 1; i++) {
                    int lineY = (i * lineHeight) + ((i - 1) * lineSpacing);
                    canvas.drawText(dropCapLines.get(i - 1), dropCapLinesX, lineY, paint);
                }
            }

            if (lines != null) {
                paint.setTextSize(textSize);

                for (int i = 1; i < lines.size() + 1; i++) {
                    int lineY = dropCap.getHeight() + lineSpacing + (i * lineHeight) + ((i - 1) * lineSpacing);
                    canvas.drawText(lines.get(i - 1), 0, lineY, paint);
                }
            }
        } else if (lines != null) {
            paint.setTextSize(textSize);

            for (int i = 1; i < lines.size() + 1; i++) {
                int lineY = (i * lineHeight) + ((i - 1) * lineSpacing);
                canvas.drawText(lines.get(i - 1), 0, lineY, paint);
            }
        }
    }
}
