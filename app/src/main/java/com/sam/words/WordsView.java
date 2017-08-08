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

    Typeface crimsonText;
    Typeface crimsonTextBold;
    Typeface crimsonTextItalic;

    List<Line> page = new ArrayList<>();

    private int firstLetterWidth;
    private int sentenceHeight;
    private Paint mTextPaint;
    private String title = "Title";
    private String author = "Author";
    private String content = "Once upon a time...";
    private int pageNumber = 0;
    private List<String> lines;
    private List<Rect> sentenceBounds;
    private int textSizePx;
    private int viewY = 0;
    private int viewWidth = 0;
    private int viewX = 0;
    private int viewHeight = 0;
    private int textAreaLeft;
    private int textAreaRight;
    private int textAreaTop;
    private int textAreaBottom;
    private float lineSeperation = 1.7f;

    private int linesPerDropCap = 3;
    private int dropCapWidth;
    private int dropCapHeight;
    private int dropCapTextSize;


    public WordsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        crimsonText = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf"
        );

        crimsonTextBold = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/CrimsonText/CrimsonText-Bold.ttf"
        );

        crimsonTextItalic = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/CrimsonText/CrimsonText-Italic.ttf"
        );

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        int defaultTextSize = getResources().getInteger(R.integer.default_text_size);

        textSizePx = sharedPref.getInt(
                context.getString(R.string.saved_text_size), defaultTextSize);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        calculateDimensions();

        if (content.length() > 0) {

            mTextPaint.setTypeface(crimsonText);

            float savedTextHeight = mTextPaint.getTextSize();

            canvas.drawText(
                    title,
                    textAreaLeft,
                    viewY + savedTextHeight,
                    mTextPaint);

            canvas.drawText(
                    author,
                    textAreaLeft,
                    viewY,
                    mTextPaint);

            canvas.drawText(
                    String.valueOf(pageNumber),
                    viewHeight,
                    viewWidth,
                    mTextPaint);

            mTextPaint.setTextSize(dropCapTextSize);
            canvas.drawText(
                    String.valueOf(content.charAt(0)),
                    textAreaLeft,
                    textAreaTop + dropCapHeight,
                    mTextPaint);
            mTextPaint.setTextSize(savedTextHeight);

            // TODO - Don't calculate this on every draw
            lines = lineWrap(mTextPaint, textAreaRight - textAreaLeft - dropCapWidth - lineSpacing, content.substring(1, content.length()));

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
        }*/

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
                viewY + 5,
                viewX + 5,
                viewWidth - 5,
                viewHeight - 5,
                mTextPaint
        );
    }

    private int getLineHeight() {
        Rect bounds = new Rect();
        mTextPaint.getTextBounds("A", 0, 1, bounds);
        return bounds.height();
    }

    private List<Line> getLines(Typeface typeface, int textSize, int textAreaX, int textAreaY, String text) {

        List<Line> list = new ArrayList<>();

        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextSize(textSize);

        List<String> lines = lineWrap(mTextPaint, viewWidth - textAreaX, text);

        for (int i = 1; i < lines.size() + 1; i++) {
            String line = lines.get(i - 1);

            int lineHeight = getLineHeight();
            int lineSpacing = (int) ((lineHeight * lineSeperation) - lineHeight);

            int lineY = textAreaY + (i * lineHeight) + ((i - 1) * lineSpacing);

            list.add(new Line(textAreaX, lineY, textSize, typeface, line));
        }

        return list;
    }

    /**
     * Calculate (or re-calculate) the dimensions required for drawing
     * Each time data changes, call this once to set up all subsequent draws
     */
    private List<Line> calculateDimensions() {
        viewX = 0;
        viewY = 0;
        viewWidth = getWidth();
        viewHeight = getHeight();

        page = new ArrayList<>();

        titleLines = getLines(crimsonTextBold, textSizePx, viewX, viewY, title);

        page.addAll();
        page.addAll(getLines(crimsonTextItalic, textSizePx, author));
        page.addAll(getLines(crimsonText, textSizePx, content));



        /*dropCapHeight = (linesPerDropCap * lineHeight) + ((linesPerDropCap - 1) * lineSpacing);
        dropCapTextSize = getFontSizeToMatchLineHeight(mTextPaint, dropCapHeight);
        mTextPaint.setTextSize(dropCapTextSize);
        mTextPaint.getTextBounds("A", 0, 1, bounds);
        dropCapWidth = bounds.width();
        mTextPaint.setTextSize(textSizePx);*/

        //sentenceBounds = getBoundsPerLetter(mTextPaint, content);
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
        textSizePx += amount;
        calculateDimensions();
    }

    public void decreaseTextSize(int amount) {
        textSizePx -= amount;
        calculateDimensions();
    }

    public void setText(String text) {
        content = text;
    }

    public String getText() {
        return content;
    }

    public int getTextSize() {
        return textSizePx;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
