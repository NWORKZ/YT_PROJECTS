package com.nxiv.inlaypresentor.presentation;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nxiv.inlaypresentor.camera.AppCamera;

import java.io.IOException;

public class PresentationPreview extends SurfaceView implements SurfaceHolder.Callback {

    private AppCamera mCamera;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Slide slide;
    private Context context;


    public PresentationPreview(Context context, AppCamera camera) {
        super(context);
        setWillNotDraw(false);

        //this.context = context;
        mCamera = camera;

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        connectPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(surfaceHolder != null) connectPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //if(mCamera != null) mCamera.getCamera().release();
    }

    //render here
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //Slide currentSlide = new Slide();

        this.canvas = canvas;

        for (Widget widget: slide.widgets) {
            widget.draw(canvas);
        }

    }

    public void setCamera(AppCamera camera){
        mCamera = camera;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void connectPreview(){
        try {
            if(mCamera != null) {
                if (mCamera.getCamera() != null) {
                    mCamera.getCamera().setPreviewDisplay(surfaceHolder);
                    mCamera.getCamera().startPreview();
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, "Start camera preview failed", Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
        }
    }

    public Slide getSlide() {
        return slide;
    }

    public void setSlide(Slide slide) {
        this.slide = slide;
    }
}
