package com.rincliu.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.widget.TextView;

public class RLAutoSplitTextView extends TextView
{

    public RLAutoSplitTextView(Context context)
    {
        super(context);
    }

    public RLAutoSplitTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RLAutoSplitTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Paint mPaint = getPaint();
        
        //Get font's height & leading:
        FontMetrics fm = mPaint.getFontMetrics();
        float height = fm.descent - fm.ascent + fm.leading;
        
        float x = 0;
        float y = height;
        String[] textLines = autoSplit(getText().toString(), mPaint, getWidth());
        for (String textLine : textLines)
        {
            canvas.drawText(textLine, x, y, mPaint);
            y += height;
        }
    }

    private String[] autoSplit(String content, Paint paint, float containerWidth)
    {
        int length = content.length();
        float textWidth = paint.measureText(content);
        
        if (textWidth <= containerWidth)
        {
            return new String[] {content};
        }
        
        int lines = (int) Math.ceil(textWidth / containerWidth);
        String[] textLines = new String[lines];
        
        int start = 0, end = 1, i = 0;
        while (start < length)
        {
            float currentTextWidth = paint.measureText(content, start, end);
            if (currentTextWidth > containerWidth)
            {
                textLines[i++] = content.substring(start, end);
                start = end;
            }
            if (end == length)
            {
                textLines[i] = content.substring(start, end);
                break;
            }
            end++;
        }
        return textLines;
    }
}