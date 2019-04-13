package com.nxiv.inlaypresentor.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppCamera {

    //CameraPreview preview;
    private Camera camera;
    private static boolean isBackFacing;

    public AppCamera(int camIndex){
        isBackFacing = true;
        openCamera(camIndex);
    }

    public void openCamera(final int camIndex){
        new Runnable(){
            @Override
            public void run() {
                try {
                    releaseCamera();
                    camera = Camera.open(camIndex);
                    setBestPreview();

                    Log.i("CAMERA", "OPEN SUCCESS");
                }catch (Exception e){
                    Log.e("CAMERA", "OPEN FAILED");

                }
            }
        }.run();
    }

    public void releaseCamera(){
        if(camera != null){
            //preview.release();
            camera.release();
            camera = null;
        }
    }

    public Camera getCamera(){
        return camera;
    }

    private void setBestPreview(){
        List<Camera.Size> previewSize = camera.getParameters().getSupportedPreviewSizes();
        List<Camera.Size> pictureSize = camera.getParameters().getSupportedPictureSizes();

        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(previewSize.get(previewSize.size() -1 ).width
                , previewSize.get(previewSize.size() -1 ).height);
        parameters.setPictureSize(pictureSize.get(pictureSize.size() -1 ).width
                , pictureSize.get(pictureSize.size() -1 ).height);
    }

    public void switchCamera(){
        if(camera != null){
            Camera.CameraInfo camInfo = new Camera.CameraInfo();

            int camCount = Camera.getNumberOfCameras();
            Log.i("CAMERA COUNT", camCount + "");
            for(int x = 0; x < camCount; x++){
                Camera.getCameraInfo(x, camInfo);
                if(camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && isBackFacing){
                    Log.i("SWITCH CAMERA","SWITCHED TO FRONT");
                    openCamera(x);
                    isBackFacing = false;
                    break;
                }else if(camInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK && !isBackFacing){
                    Log.i("SWITCH CAMERA","SWITCHED TO BACK");
                    openCamera(x);
                    isBackFacing = true;
                    break;
                }
            }
        }
    }


    public boolean isBackFacing() {
        return isBackFacing;
    }

    public void setBackFacing(boolean backFacing) {
        isBackFacing = backFacing;
    }

    /*public CameraPreview getPreview() {
        return preview;
    }*/
}
