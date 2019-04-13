package com.nxiv.inlaypresentor.presentation;

import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceView;

import com.nxiv.inlaypresentor.camera.AppCamera;
import com.nxiv.inlaypresentor.permissions.AudioPermission;

import java.io.IOException;

public class Recorder {

    private MediaRecorder mediaRecorder;
    private AppCamera mCamera;

    public Recorder(AppCamera camera){
        mCamera = camera;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(camera.getCamera());
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    }

    public void record(String filename){

        try {

            if(mediaRecorder == null){
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setCamera(mCamera.getCamera());
                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            }

            mediaRecorder.setOutputFile(filename);
            mediaRecorder.prepare();
            mediaRecorder.start();
        }catch (IOException e){
            Log.i("MEDIA RECORDER:", "PREPARE FAILED");
        }

    }

    public void stop(){
        if(mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
        }
    }
}
