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

    private Paint mTextPaint;
    private Typeface typeface;
    private Page page;
    private int textSize;
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 500;
        int desiredHeight = 500;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        page.draw(canvas, mTextPaint);

        // Debug view border
        //canvas.drawRect(0, 0, viewWidth-1, viewHeight-1, paint);
    }

    protected List<Page> calculatePages(List<Chapter> chapters) {

        List<Page> pages = new ArrayList<>();

        int viewX = 0;
        int viewY = 0;
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextSize(textSize);

        int lineHeight = getTextHeight(mTextPaint, "I");
        int lineSpacing = (int) ((lineHeight * lineSeperation) - lineHeight);

        for (Chapter chapter : chapters) {

            Page newPage = new Page(typeface, textSize, lineHeight, lineSeperation);
            String chapterContent = chapter.getContent();

            // Step 1 - Chapter title

            // TODO - Title layout
            newPage.setChapterTitle(chapter.getTitle());

            // Step 2 - Drop cap

            if (chapterContent.length() > 0) {

                char dChar = chapterContent.charAt(0);
                int dWidth = getTextWidth(mTextPaint, String.valueOf(dChar));
                int dHeight = (linesPerDropCap * lineHeight) + ((linesPerDropCap - 1) * lineSpacing);
                int dTextSize = getFontSizeToMatchLineHeight(mTextPaint, dHeight);

                mTextPaint.setTextSize(dTextSize);
                newPage.setDropCap(new DropCap(dChar, dWidth, dHeight, dTextSize));

                // Step 3 - Lines around drop cap

                if (chapterContent.length() > 1) {

                    mTextPaint.setTextSize(textSize);
                    int dropCapLinesX = viewX + dWidth + lineSpacing;

                    List<String> lines = lineWrap(mTextPaint, viewWidth - dropCapLinesX, chapterContent.substring(1, chapterContent.length()));
                    newPage.setDropCapLines(lines.subList(0, Math.min(lines.size(), linesPerDropCap)));

                    // Step 4 - Remaining lines

                    if (lines.size() > linesPerDropCap) {

                        String remainingText = TextUtils.join(" ", lines.subList(linesPerDropCap, lines.size()));
                        newPage.setLines(lineWrap(mTextPaint, viewWidth, remainingText));
                    }
                }
            }

            pages.add(newPage);
        }

        return pages;
    }

    int getTextHeight(Paint paint, String text) {
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

    public List<Page> setText(List<Chapter> chapters) {
        List<Page> pages = calculatePages(chapters);

        // FIXME - get the right page
        page = pages.get(0);

        return pages;
    }

    public int getTextSize() {
        return textSize;
    }
}
