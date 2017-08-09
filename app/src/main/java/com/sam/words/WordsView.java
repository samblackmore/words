package com.sam.words;

import android.content.Context;
import android.content.SharedPreferences;
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

    private int textSize;
    private String content;
    private Paint mTextPaint;
    private Typeface typeface;

    private float lineSeperation = 1.7f;
    private int linesPerDropCap = 3;

    public WordsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        typeface = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf"
        );

        textSize = SharedPreferencesHelper.getTextSize(context);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewX = 0;
        int viewY = 0;
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextSize(textSize);

        int lineHeight = getTextHeight(mTextPaint, "I");
        int lineSpacing = (int) ((lineHeight * lineSeperation) - lineHeight);

        String dropCap;
        int dropCapWidth = 0;
        int dropCapHeight = (linesPerDropCap * lineHeight) + ((linesPerDropCap - 1) * lineSpacing);

        if (content.length() > 0) {

            // Start by drawing the drop cap

            mTextPaint.setTextSize(getFontSizeToMatchLineHeight(mTextPaint, dropCapHeight));

            dropCap = String.valueOf(content.charAt(0));
            dropCapWidth = getTextWidth(mTextPaint, dropCap);

            canvas.drawText(
                    dropCap,
                    viewX,
                    viewY + dropCapHeight,
                    mTextPaint);
        }

        if (content.length() > 1) {

            // Draw the first lines that wrap around the drop cap

            mTextPaint.setTextSize(textSize);

            int dropCapLinesX = viewX + dropCapWidth + lineSpacing;

            List<String> lines = lineWrap(mTextPaint, viewWidth - dropCapLinesX, content.substring(1, content.length()));
            List<String> dropCapLines = lines.subList(0, Math.min(lines.size(), linesPerDropCap));

            for (int i = 1; i < dropCapLines.size() + 1; i++) {
                canvas.drawText(
                        dropCapLines.get(i - 1),
                        dropCapLinesX,
                        viewY + (i * lineHeight) + ((i - 1) * lineSpacing),
                        mTextPaint);
            }

            // Draw any remaining lines under the drop cap

            if (lines.size() > linesPerDropCap) {

                String remainingText = TextUtils.join(" ", lines.subList(linesPerDropCap, lines.size()));
                lines = lineWrap(mTextPaint, viewWidth, remainingText);

                for (int i = 1; i < lines.size() + 1; i++) {
                    canvas.drawText(
                            lines.get(i - 1),
                            viewX,
                            viewY + dropCapHeight + lineSpacing + (i * lineHeight) + ((i - 1) * lineSpacing),
                            mTextPaint);
                }
            }
        }

        // Debug view border
        // canvas.drawRect(viewX, viewY, viewWidth, viewHeight, mTextPaint);
    }

    private int getTextHeight(Paint paint, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, 1, bounds);
        return bounds.height();
    }

    private int getTextWidth(Paint paint, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, 1, bounds);
        return bounds.height();
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

    /**
     * Split a string over multiple lines if it's too long
     * @param paint The paint instance whose font we're using
     * @param widthAvailable The width in pixels to wrap after
     * @param string The text to wrap
     * @return a list of strings representing the lines after wrapping
     */
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
        textSize += amount;
    }

    public void decreaseTextSize(int amount) {
        textSize -= amount;
    }

    public void setText(String text) {
        content = text;
    }

    public String getText() {
        return content;
    }

    public int getTextSize() {
        return textSize;
    }
}
