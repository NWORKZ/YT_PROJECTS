package com.nxiv.inlaypresentor.presentation.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nxiv.inlaypresentor.presentation.Widget;

import org.w3c.dom.Text;

public class TextWidget extends Widget{

    private String value;
    private Paint paint;
    //private Canvas canvas;

    private final static String type = "TEXT_WIDGET";

    public TextWidget(String value) {
        this.value = value;
    }

    public TextWidget(int x, int y) {
        super(0, 0, x, y);
    }

    public TextWidget(int w, int h, int x, int y, String value, Paint paint) {
        super(w, h, x, y);
        this.paint = paint;
        this.value = value;
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        super.draw(canvas);
        canvas.drawText(value, super.getX(), super.getY(), paint);

        canvas.restore();
    }

    @Override
    public String getType() {
        //return super.getType();
        return type;
    }

    @Override
    public boolean isYReversed() {
        return super.isYReversed();
    }

    @Override
    public boolean click(MotionEvent event) {
        //super.click(event);

        float eventX = event.getX();
        float eventY = event.getY();

        if((eventX >= super.getX() && eventX <= super.getX() + super.getWidth()) &&
            (eventY <= super.getY() && eventY >= super.getY() - super.getHeight()) ) {
            super.willDrawBounds(true, false);
            super.setIsClicked(true);
            return true;
        }else{
            super.willDrawBounds(false, false);
            super.setIsClicked(false);
        }

        return false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
