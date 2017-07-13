package com.sam.words;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class WordsView extends View {

    private int firstLetterWidth;
    private int sentenceHeight;
    private Paint mTextPaint;
    private String sentence = "The quick brown fox jumps over the lazy dog";
    private List<String> lines;
    private List<Rect> sentenceBounds;
    private int paddingHor = 100;
    private int paddingVer = 200;
    private int viewLeft = -1;
    private int viewRight = -1;
    private int viewTop = -1;
    private int viewBottom = -1;
    private int textAreaLeft;
    private int textAreaRight;
    private int textAreaTop;
    private int textAreaBottom;
    private float lineSeperation = 1.5f;
    private int lineSpacing;
    private int lineHeight;

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
        String line = "";

        // For each next word, see if we can add it to the line
        for (String nextWord : string.split(" ")) {;
            String lineWithNextWord = line + nextWord;
            float testWidth = paint.measureText(lineWithNextWord, 0, lineWithNextWord.length());
            // If proposed line is too long
            if (widthAvailable - testWidth <= 0) {
                list.add(line);
                line = nextWord;
            }
            else line = lineWithNextWord;
        }
        return list;
    }


    public WordsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface crimsonText = Typeface.createFromAsset(
                context.getAssets(),
                "fonts/CrimsonText/CrimsonText-Regular.ttf"
        );
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(crimsonText);
        mTextPaint.setTextSize(300);
        mTextPaint.setStyle(Paint.Style.STROKE);

        // Line height is height of the tallest letter - l
        Rect bounds = new Rect();
        mTextPaint.getTextBounds("l", 0, 1, bounds);
        lineHeight = bounds.height();
        lineSpacing = (int) ((lineHeight * lineSeperation) - lineHeight);

        //sentenceBounds = getBoundsPerLetter(mTextPaint, sentence);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculateDimensions();

        lines = lineWrap(mTextPaint, textAreaRight - textAreaLeft, sentence);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i-1);
            canvas.drawText(
                    line,
                    textAreaLeft,
                    textAreaTop + (i*lineHeight) + ((i-1) * lineSpacing),
                    mTextPaint);
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
                viewLeft + 5,
                viewTop + 5,
                viewRight - 5,
                viewBottom - 5,
                mTextPaint
        );
    }
}
