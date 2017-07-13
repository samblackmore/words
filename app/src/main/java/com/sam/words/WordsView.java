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
    private String sentence = "AalqQ";
    private List<Rect> sentenceBounds;
    private int paddingHor = 100;
    private int paddingVer = 200;
    private int viewLeft = -1;
    private int viewRight = -1;
    private int viewTop = -1;
    private int viewBottom = -1;

    private List<Rect> getBoundsPerLetter(Paint paint, String string) {
        List<Rect> list = new ArrayList<>();
        for (int i = 0; i < string.length(); i++) {
            Rect bounds = new Rect();
            paint.getTextBounds(string, i, i + 1, bounds);
            float width = paint.measureText(string, i, i + 1);
            bounds.set(0, 0, (int) width, bounds.height());
            list.add(bounds);
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
        sentenceBounds = getBoundsPerLetter(mTextPaint, sentence);
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(sentence, 0, sentence.length()-1, bounds);
        sentenceHeight = bounds.height();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int[] coords = {-1, -1};
        this.getLocationInWindow(coords);

        if (viewLeft == -1)
            viewLeft = getLeft();
        if (viewRight == -1)
            viewRight = getRight();
        if (viewTop == -1)
            viewTop = getTop();
        if (viewBottom == - 1)
            viewBottom  = getBottom();

        int textX = viewLeft + paddingHor;
        int textY = viewTop + paddingVer + 200;

        canvas.drawText(
                sentence,
                textX,
                textY,
                mTextPaint);

        int nextX = textX;
        for (Rect rect : sentenceBounds) {
            canvas.drawRect(
                    nextX,
                    textY,
                    nextX + rect.width(),
                    textY - rect.height(),
                    mTextPaint);
            nextX += rect.width();
        }

        canvas.drawRect(
                viewLeft + 5,
                viewTop + 5,
                viewRight - 5,
                viewBottom - 5,
                mTextPaint
        );
    }
}
