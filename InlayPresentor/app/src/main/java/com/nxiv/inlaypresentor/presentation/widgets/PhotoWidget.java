package com.nxiv.inlaypresentor.presentation.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.nxiv.inlaypresentor.presentation.Widget;

public class PhotoWidget extends Widget{

    private Bitmap bitmap;
    private Paint paint;

    private final static String type = "PHOTO_WIDGET";

    public PhotoWidget( int width, int height, int x, int y, Bitmap bitmap, Paint paint) {
        super(width, height, x, y);
        this.bitmap = bitmap;
        this.paint = paint;
    }

    public PhotoWidget(int width, int height, int x, int y, Bitmap bitmap) {
        super(width, height, x, y);
        this.bitmap = bitmap;
    }

    @Override
    public boolean isYReversed() {
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        super.draw(canvas);
        canvas.drawBitmap(bitmap, super.getX(), super.getY(), paint);

        canvas.restore();

    }

    @Override
    public String getType() {
        //return super.getType();
        return type;
    }

    @Override
    public boolean click(MotionEvent event) {
        //super.click(event);

        float eventX = event.getX();
        float eventY = event.getY();

        if((eventX >= super.getX() && eventX <= super.getX() + super.getWidth()) &&
                (eventY >= super.getY() && eventY <= super.getY() + super.getHeight()) ) {
            super.willDrawBounds(true, true);
            super.setIsClicked(true);
            return true;
        }else{
            super.willDrawBounds(false, false);
            super.setIsClicked(false);
        }

        return false;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }
}
