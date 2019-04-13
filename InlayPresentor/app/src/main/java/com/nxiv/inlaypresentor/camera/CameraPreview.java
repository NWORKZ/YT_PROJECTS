package com.nxiv.inlaypresentor.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

//don't implement this yet on any activity
//until you can create the slides
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    //private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private Context context;
    //private DrawingPreview overlay;

    public CameraPreview(Context context, Camera camera){
        super(context);
        this.context = context;
        mCamera = camera;

        //surfaceView = new SurfaceView(context);
        //addView(surfaceView);

        //overlay = new DrawingPreview(context);
        //addView(overlay);

        setWillNotDraw(false);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Camera.Parameters parameters = mCamera.getParameters();

        if(surfaceHolder == null) return;

        mCamera.stopPreview();

        try{
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void release(){
        mCamera.setPreviewCallback(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Toast.makeText(this.context, "", Toast.LENGTH_SHORT).show();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawRect(new Rect(150,150, 500,500), paint);
    }
}
