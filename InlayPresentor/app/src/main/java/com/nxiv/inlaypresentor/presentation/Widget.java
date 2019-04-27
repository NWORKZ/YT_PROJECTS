package com.nxiv.inlaypresentor.presentation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Widget implements WidgetDraw{

    private int width, height;
    private int x, y;
    private Paint boundPaint;
    private boolean drawBounds , isReverse;
    private boolean isClicked;
    private Canvas canvas;
    private float rotation, scale;
    private Point center;
    private boolean willRotate, willScale;
    private String value;
    private String type;

    public Widget(){};

    public Widget(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        boundPaint = new Paint();
        boundPaint.setStyle(Paint.Style.STROKE);
        boundPaint.setColor(Color.YELLOW);
        boundPaint.setStrokeWidth(4);

        scale = 1;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    protected Paint getBoundPaint() {
        return boundPaint;
    }

    public boolean isDrawBounds() {
        return drawBounds;
    }

    public void willDrawBounds(boolean draw, boolean reverse){
        drawBounds = draw;
        isReverse = reverse;
    }

    @Override
    public void draw(Canvas canvas) {
        //Log.i("WIDGET", "draw: I am a parent class i do nothing here");
       // if(rotation != 0) {
            if (!isReverse) canvas.rotate(rotation, x + (width / 2), y - (height / 2));
            else canvas.rotate(rotation, x + (width / 2), y + (height / 2));
       //

        //if(scale != 1) {
            //canvas.translate(canvas.getWidth()/2, canvas.getHeight()/2);
            if(!isReverse) canvas.scale(scale, scale, x + (width/2), y - (height/2));
            else canvas.scale(scale, scale, x + (width/2), y + (height/2));

        //}

        this.canvas = canvas;
        if(drawBounds){
            if(!isReverse) {
                this.canvas.drawRect(new Rect(x
                        , y - height
                        , x + width
                        , y), boundPaint);
            }else{
                this.canvas.drawRect(new Rect(x
                        , y
                        , x + width
                        , y + height) , boundPaint);
            }
        }
    }

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    public boolean isYReversed(){
        return false;
    }

    public boolean click(MotionEvent event){
        Log.i("parent widdget","doing nothing");
        return false;
    }

    public void setIsClicked(boolean clicked){
        isClicked = clicked;
    }

    public boolean isClicked(){
        return isClicked;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
       this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Point getCenter() {
        return center;
    }

    public boolean isWillRotate() {
        return willRotate;
    }

    public void setWillRotate(boolean willRotate) {
        this.willRotate = willRotate;
    }

    public boolean isWillScale() {
        return willScale;
    }

    public void setWillScale(boolean willScale) {
        this.willScale = willScale;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Paint getPaint(){
        return boundPaint;
    }
}
