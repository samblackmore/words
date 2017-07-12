package com.sam.words;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class WordsView extends View {

    private int firstLetterWidth;
    private int firstLetterHeight;
    private Paint mTextPaint;
    private String firstLetter = "A";
    private int firstLetterX = 50;
    private int firstLetterY = 300;


    public WordsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(300);
        mTextPaint.setStyle(Paint.Style.STROKE);
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(firstLetter, 0, 1, bounds);
        firstLetterWidth = bounds.width();
        firstLetterHeight = bounds.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(firstLetter, firstLetterX, firstLetterY, mTextPaint);
        canvas.drawRect(
                firstLetterX,
                firstLetterY - firstLetterHeight,
                firstLetterX + firstLetterWidth,
                firstLetterY,
                mTextPaint);
    }
}
