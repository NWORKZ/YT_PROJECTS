package com.nxiv.inlaypresentor;

import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nxiv.inlaypresentor.camera.AppCamera;
import com.nxiv.inlaypresentor.permissions.AudioPermission;
import com.nxiv.inlaypresentor.permissions.ExternalStoragePermission;
import com.nxiv.inlaypresentor.presentation.Presentation;
import com.nxiv.inlaypresentor.presentation.PresentationPreview;
import com.nxiv.inlaypresentor.presentation.Recorder;
import com.nxiv.inlaypresentor.presentation.Slide;
import com.nxiv.inlaypresentor.presentation.utility.PresentationFileHandler;

import java.io.File;

public class PresentActivity extends AppCompatActivity {

    Presentation presentation;
    PresentationFileHandler fileHandler;

    //
    private int slideIndex;
    //
    private AppCamera appCamera;
    private PresentationPreview preview;

    Slide currentSlide;

    private ImageView switchCamera;
    private ImageView nextSlide, prevSlide;
    private ImageView recordButton;
    private RelativeLayout previewContainer;

    Recorder recorder;
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        fileHandler = new PresentationFileHandler(this);

        new Runnable() {
            @Override
            public void run() {
                presentation =  fileHandler.getPresentation("asd.iyp");
                currentSlide = presentation.getSlides().get(0);
            }
        }.run();

        presentation.getSlides().add(currentSlide);//add first slide

        //presentation preview
        appCamera = new AppCamera(0);
        preview = new PresentationPreview(this, appCamera);
        preview.setSlide(currentSlide);

        initializeUI();
        initializeUIInteraction();

        recorder = new Recorder(appCamera);
        previewContainer.addView(preview);
    }

    private void initializeUI(){
        previewContainer = findViewById(R.id.PPresentationPreviewContainer);
        //next-prev
        nextSlide = findViewById(R.id.NextSlide);
        prevSlide = findViewById(R.id.PreviousSlide);
        //camera
        switchCamera = findViewById(R.id.SwitchCamera);

        //
        recordButton = findViewById(R.id.RecordButton);
    }

    private void initializeUIInteraction(){
        //camera
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Runnable() {
                    @Override
                    public void run() {
                        if(appCamera.getCamera() != null){
                            //appCamera.releaseCamera();
                            preview.setCamera(null);
                            appCamera.switchCamera();

                            previewContainer.removeAllViews();

                            //preview = new PresentationPreview(NewPresentationActivity.this, appCamera);
                            preview.setCamera(appCamera);
                            preview.setSlide(currentSlide);
                            previewContainer.addView(preview);
                        }
                    }
                }.run();

            }
        });

        //next-prev
        nextSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NewPresentationActivity.this, "next slide", Toast.LENGTH_SHORT).show();

                if(slideIndex < presentation.getSlides().size() - 1){
                    slideIndex++;
                }else if(slideIndex+1 >= presentation.getSlides().size()){
                    slideIndex = presentation.getSlides().size() - 1;
                }

                currentSlide = presentation.getSlides().get(slideIndex);
                preview.setSlide(presentation.getSlides().get(slideIndex));
                preview.invalidate();
            }
        });

        prevSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NewPresentationActivity.this, "prev slide", Toast.LENGTH_SHORT).show();

                if(slideIndex-1 < 0){
                    slideIndex = 0;
                }else{
                    slideIndex--;
                }

                currentSlide = presentation.getSlides().get(slideIndex);
                preview.setSlide(presentation.getSlides().get(slideIndex));
                preview.invalidate();
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording){
                    Toast.makeText(PresentActivity.this, "Recording stop", Toast.LENGTH_SHORT).show();
                    isRecording = false;
                    recorder.stop();
                }else{

                    AudioPermission audioPermission = new AudioPermission(PresentActivity.this);
                    ExternalStoragePermission externalStoragePermission = new ExternalStoragePermission(PresentActivity.this);

                    boolean externalPermitted = false;
                    boolean audioPermitted = false;

                    if(!externalStoragePermission.check())audioPermission.request(PresentActivity.this);
                    else externalPermitted = true;

                    if(!audioPermission.check()) audioPermission.request(PresentActivity.this);
                    else audioPermitted = true;


                    if(externalPermitted && audioPermitted){
                        //just testing it out
                        Toast.makeText(PresentActivity.this, "Recording Start", Toast.LENGTH_SHORT).show();
                        File file = PresentationFileHandler.getPublicAlbumStorageDir("record");
                        recorder.record(file.getPath() + "/test.mp4");
                        isRecording = true;
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(appCamera.getCamera() == null) {
            appCamera.openCamera(0);
            preview.setCamera(appCamera);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(appCamera.getCamera() == null) {
            appCamera.openCamera(0);
            preview.setCamera(appCamera);
        }

    }

    @Override
    protected void onPause() {
        if(appCamera.getCamera() != null){
            appCamera.releaseCamera();
            preview.setCamera(null);
        }
        super.onPause();
    }
}
