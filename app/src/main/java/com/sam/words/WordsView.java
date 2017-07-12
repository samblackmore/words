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
    private int paddingHor = 100;
    private int paddingVer = 200;
    private int viewLeft = -1;
    private int viewRight = -1;
    private int viewTop = -1;
    private int viewBottom = -1;


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
        int textY = viewTop + paddingVer;

        canvas.drawText(
                firstLetter,
                textX,
                textY + firstLetterHeight,
                mTextPaint);
        canvas.drawRect(
                textX,
                textY,
                textX + firstLetterWidth,
                textY + firstLetterHeight,
                mTextPaint);

        canvas.drawRect(
                viewLeft + 5,
                viewTop + 5,
                viewRight - 5,
                viewBottom - 5,
                mTextPaint
        );
    }
}
