package com.sam.words.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.List;

/**
 * A page in a story. The first page in a chapter has a chapter title, a drop cap, and lines around
 * the drop cap. The remaining pages have only lines.
 */

public class Page {
    private Typeface typeface;
    private int textSize;
    private int lineHeight;
    private int lineSpacing;
    private String chapterTitle;
    private DropCap dropCap;
    private List<String> dropCapLines;
    private List<String> lines;

    Page(Typeface typeface, int textSize, int lineHeight, float lineSeparation) {
        this.typeface = typeface;
        this.textSize = textSize;
        this.lineHeight = lineHeight;

        lineSpacing = (int) ((lineHeight * lineSeparation) - lineHeight);
    }

    void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    void setDropCap(DropCap dropCap) {
        this.dropCap = dropCap;
    }

    void setDropCapLines(List<String> dropCapLines) {
        this.dropCapLines = dropCapLines;
    }

    void setLines(List<String> lines) {
        this.lines = lines;
    }

    private void drawLines(Canvas canvas, Paint paint, int x, int y, List<String> lines) {
        for (int i = 1; i < lines.size() + 1; i++) {
            int lineY = (i * lineHeight) + ((i - 1) * lineSpacing);
            canvas.drawText(lines.get(i - 1), x, y + lineY, paint);
        }
    }

    int getWordsBottom() {
        int lineCount = (lines == null ? 0 : lines.size()) + (dropCapLines == null ? 0 : dropCapLines.size());
        return (lineCount * lineHeight) + ((lineCount - 1) * lineSpacing);
    }

    void draw(Canvas canvas, Paint paint) {

        paint.setTypeface(typeface);

        if (dropCap != null) {
            paint.setTextSize(dropCap.getTextSize());
            canvas.drawText(dropCap.toString(), 0, dropCap.getHeight(), paint);

            if (dropCapLines != null) {
                paint.setTextSize(textSize);
                drawLines(canvas, paint, dropCap.getWidth() + lineSpacing, 0, dropCapLines);
            }

            if (lines != null) {
                paint.setTextSize(textSize);
                drawLines(canvas, paint, 0, dropCap.getHeight() + lineSpacing, lines);
            }
        } else if (lines != null) {
            paint.setTextSize(textSize);
            drawLines(canvas, paint, 0, 0, lines);
        }
    }
}
