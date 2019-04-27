package com.nxiv.inlaypresentor.presentation;

import android.media.MediaRecorder;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import com.nxiv.inlaypresentor.camera.AppCamera;
import com.nxiv.inlaypresentor.permissions.AudioPermission;

import java.io.IOException;

public class Recorder {

    private MediaRecorder mediaRecorder;
    private AppCamera mCamera;
    private boolean prepareFailed;

    public Recorder(AppCamera camera){
        mCamera = camera;
        /*mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(camera.getCamera());
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);*/

    }

    public void record(String filename, SurfaceView surfaceView){

        try {

            if(mediaRecorder == null){
                mediaRecorder = new MediaRecorder();
                //mCamera.getCamera().unlock();
                //mediaRecorder.setCamera(mCamera.getCamera());
                mediaRecorder.setInputSurface(surfaceView.getHolder().getSurface());
                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            }

            mediaRecorder.setOutputFile(filename);
            mediaRecorder.prepare();
            mediaRecorder.start();
            prepareFailed = false;
            Log.i("MEDIA RECORDER:", "PREPARE SUCCESS");
        }catch (Exception e){
            prepareFailed = true;
            Log.i("MEDIA RECORDER:", "PREPARE FAILED " + e.getMessage());
            //e.printStackTrace();
        }

    }

    public void stop(){
        if(mediaRecorder != null && !prepareFailed) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mCamera.getCamera().lock();
            mediaRecorder = null;
        }
    }
}
