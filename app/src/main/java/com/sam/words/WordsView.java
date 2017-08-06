package com.sam.words;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WordsView extends View {

    private int firstLetterWidth;
    private int sentenceHeight;
    private Paint mTextPaint;
    private String sentence = "The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog! The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog! The quick brown fox jumps over the lazy dog ";
    private List<String> lines;
    private List<Rect> sentenceBounds;
    private int paddingHor = 0;
    private int paddingVer = 0;
    private int textSizePx = 30;
    private int viewLeft = -1;
    private int viewRight = -1;
    private int viewTop = -1;
    private int viewBottom = -1;
    private int textAreaLeft;
    private int textAreaRight;
    private int textAreaTop;
    private int textAreaBottom;
    private float lineSeperation = 1.7f;
    private int lineSpacing;
    private int lineHeight;
    private int linesPerDropCap = 3;
    private int dropCapWidth;
    private int dropCapHeight;
    private int dropCapTextSize;

    private void calculateDimensions() {
        if (viewLeft == -1) {
            viewLeft = getLeft();
            textAreaLeft = viewLeft + paddingHor;
        }
        if (viewRight == -1) {
            viewRight = getRight();
            textAreaRight = viewRight - paddingHor;
        }
        if (viewTop == -1) {
            viewTop = getTop();
            textAreaTop = viewTop + paddingVer;
        }
        if (viewBottom == - 1) {
            viewBottom = getBottom();
            textAreaBottom = viewBottom - paddingVer;
        }
    }

    private List<Rect> getBoundsPerLetter(Paint paint, String string) {
        Rect bounds = new Rect();
        List<Rect> list = new ArrayList<>();
        for (int i = 0; i < string.length(); i++) {
            paint.getTextBounds(string, i, i + 1, bounds);
            float width = paint.measureText(string, i, i + 1);
            bounds.set(0, 0, (int) width, bounds.height());
            list.add(bounds);
        }
        return list;
    }

    private List<String> lineWrap(Paint paint, int widthAvailable, String string) {

        List<String> list = new ArrayList<>();
        String[] words = string.split(" ");
        String line = words[0];

        // For each next word, see if we can add it to the line
        for (int i = 1; i < words.length; i++) {
            String nextWord = words[i];
            String lineWithNextWord = line + " " + nextWord;
            float testWidth = paint.measureText(lineWithNextWord, 0, lineWithNextWord.length());
            // If proposed line is too long
            if (widthAvailable - testWidth <= 0) {
                list.add(line);
                line = nextWord;
            }
            else line = lineWithNextWord;
        }
        list.add(line);
        return list;
    }

    private int getFontSizeToMatchLineHeight(Paint paint, int targetLineHeight) {
        float savedTextSize = paint.getTextSize();
        int newSize = 0;
        int stepSize = 100;
        int lineHeight = 0;
        boolean goingUp = true;
        Rect bounds = new Rect();

        while (Math.abs(targetLineHeight - lineHeight) > 2) {

            paint.setTextSize(newSize);
            paint.getTextBounds("A", 0, 1, bounds);
            lineHeight = bounds.height();

            if (lineHeight < targetLineHeight) {
                if (!goingUp) {
                    goingUp = true;
                    stepSize /= 2;
                }
                newSize += stepSize;
            }
            if (lineHeight > targetLineHeight) {
                if (goingUp) {
                    goingUp = false;
                    stepSize /= 2;
                }
                newSize -= stepSize;
            }
        }
        paint.setTextSize(savedTextSize);
        return newSize;
    }

    public void increaseTextSize(int amount) {
        textSizePx += amount;
        init();
    }

    public void decreaseTextSize(int amount) {
        textSizePx -= amount;
        init();
    }

    public void setText(String text) {
        sentence = text;
    }

    public String getText() {
        return sentence;
    }

    private void init() {
        mTextPaint.setTextSize(textSizePx);

        // Line height is height of the tallest letter - l
        Rect bounds = new Rect();
        mTextPaint.getTextBounds("l", 0, 1, bounds);
        lineHeight = bounds.height();
        lineSpacing = (int) ((lineHeight * lineSeperation) - lineHeight);

        dropCapHeight = (linesPerDropCap * lineHeight) + ((linesPerDropCap - 1) * lineSpacing);
        dropCapTextSize = getFontSizeToMatchLineHeight(mTextPaint, dropCapHeight);
        mTextPaint.setTextSize(dropCapTextSize);
        mTextPaint.getTextBounds("A", 0, 1, bounds);
        dropCapWidth = bounds.width();
        mTextPaint.setTextSize(textSizePx);

        //sentenceBounds = getBoundsPerLetter(mTextPaint, sentence);
    }

    public WordsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface crimsonText = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf"
        );
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(crimsonText);
        mTextPaint.setStyle(Paint.Style.STROKE);

        paddingHor = getPaddingLeft();
        paddingVer = getPaddingTop();

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateDimensions();

        if (sentence.length() > 0) {

            float savedTextHeight = mTextPaint.getTextSize();
            mTextPaint.setTextSize(dropCapTextSize);

            canvas.drawText(
                    String.valueOf(sentence.charAt(0)),
                    textAreaLeft,
                    textAreaTop + dropCapHeight,
                    mTextPaint);
            mTextPaint.setTextSize(savedTextHeight);

            // TODO - Don't calculate this on every draw
            lines = lineWrap(mTextPaint, textAreaRight - textAreaLeft - dropCapWidth - lineSpacing, sentence.substring(1, sentence.length()));

            List<String> dropCapLines = lines.subList(0, Math.min(lines.size(), linesPerDropCap));

            for (int i = 1; i < dropCapLines.size() + 1; i++) {
                String line = dropCapLines.get(i - 1);
                canvas.drawText(
                        line,
                        textAreaLeft + dropCapWidth + lineSpacing,
                        textAreaTop + (i * lineHeight) + ((i - 1) * lineSpacing),
                        mTextPaint);
            }

            String remainingText = "";
            if (lines.size() > linesPerDropCap) {
                remainingText = TextUtils.join(" ", lines.subList(linesPerDropCap, lines.size()));
                lines = lineWrap(mTextPaint, textAreaRight - textAreaLeft, remainingText);

                for (int i = 1; i < lines.size() + 1; i++) {
                    String line = lines.get(i - 1);
                    canvas.drawText(
                            line,
                            textAreaLeft,
                            textAreaTop + dropCapHeight + lineSpacing + (i * lineHeight) + ((i - 1) * lineSpacing),
                            mTextPaint);
                }
            }
        }

        /*int nextX = textAreaLeft;
        for (Rect rect : sentenceBounds) {
            canvas.drawRect(
                    nextX,
                    textAreaTop + lineHeight,
                    nextX + rect.width(),
                    textAreaTop + lineHeight - rect.height(),
                    mTextPaint);
            nextX += rect.width();
        }

        // Text area
        canvas.drawRect(
                textAreaLeft,
                textAreaTop,
                textAreaRight,
                textAreaBottom,
                mTextPaint
        );

        // Outer border
        canvas.drawRect(
                viewLeft + 5,
                viewTop + 5,
                viewRight - 5,
                viewBottom - 5,
                mTextPaint
        );*/
    }
}
