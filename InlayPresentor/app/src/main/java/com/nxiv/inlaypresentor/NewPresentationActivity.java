package com.nxiv.inlaypresentor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.nxiv.inlaypresentor.camera.AppCamera;
import com.nxiv.inlaypresentor.permissions.ExternalStoragePermission;
import com.nxiv.inlaypresentor.presentation.Presentation;
import com.nxiv.inlaypresentor.presentation.PresentationPreview;
import com.nxiv.inlaypresentor.presentation.Slide;
import com.nxiv.inlaypresentor.presentation.Widget;
import com.nxiv.inlaypresentor.presentation.utility.PresentationFileHandler;
import com.nxiv.inlaypresentor.presentation.widgets.PhotoWidget;
import com.nxiv.inlaypresentor.presentation.widgets.TextWidget;

public class NewPresentationActivity extends AppCompatActivity{

    private Point screenSize;

    //Presentation
    private Slide currentSlide;
    private Presentation presentation;
    private int slideIndex;
    private int slideCount = 1;

    //UI
    private ImageView addText, addImage;
    private ImageView addSlide, removeSlide;
    private ImageView nextSlide, prevSlide;
    private ImageView switchCamera, toggleCamera;
    private ImageView rotateWidgetLeft, rotateWidgetRight;
    private ImageView moveWidgetForward, moveWidgetBackward;
    private ImageView removeWidget;
    private ImageView savePresentation;
    private SeekBar scaleSeekbar;

    private RelativeLayout previewContainer;

    //
    private AppCamera appCamera;
    private PresentationPreview preview;

    //
    private static final int TEXT_REQUEST_CODE = 0x9900;
    private static final int IMAGE_REQUEST_CODE = 0x9901;

    //drawing interaction
    private Widget selectedWidget, lastSelectedWidget;
    private boolean isInMotionEvent;

    //file
    PresentationFileHandler fileHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_presentation);

        screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        fileHandler = new PresentationFileHandler(NewPresentationActivity.this);

        //presentation
        Intent i = getIntent();
        final String transaction = i.getStringExtra(MainActivity.TRANSACTION_TYPE);
        final String filename = i.getStringExtra(MainActivity.PRESENTATION_FILENAME);

        if(transaction.equals("EDIT_SLIDE")){
            new Runnable() {
                @Override
                public void run() {
                    presentation =  fileHandler.getPresentation(filename);
                    currentSlide = presentation.getSlides().get(0);
                }
            }.run();

        }else{
            presentation = new Presentation();
            currentSlide = new Slide();
        }

        presentation.getSlides().add(currentSlide);//add first slide

        //presentation preview
        appCamera = new AppCamera(0);
        preview = new PresentationPreview(this, appCamera);
        preview.setSlide(currentSlide);

        initializeUI();
        initializeUIInteraction();

        previewContainer.addView(preview);
    }

    //widget creation here
    @Override
    protected void onActivityResult(final int requestCode,final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new Runnable() {
            @Override
            public void run() {

                //text activity result
                if(resultCode == Activity.RESULT_OK) {

                    if (requestCode == TEXT_REQUEST_CODE) {
                        //data
                        String text = data.getStringExtra(NewTextActivity.TEXT_EXTRA);
                        int fontSize = data.getIntExtra(NewTextActivity.TEXT_SIZE_EXTRA, 24);
                        boolean isBold = data.getBooleanExtra(NewTextActivity.TEXT_BOLD_EXTRA, false);
                        boolean isUnderline = data.getBooleanExtra(NewTextActivity.TEXT_UNDERLINE_EXTRA, false);
                        boolean isStrike = data.getBooleanExtra(NewTextActivity.TEXT_STRIKE_EXTRA, false);
                        int color = data.getIntExtra(NewTextActivity.TEXT_COLOR_EXTRA, Color.WHITE);

                        Paint paint = new Paint();
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(color);
                        paint.setTextSize(fontSize);
                        //paint.setTextScaleX(1.0f);//set to default first

                        if(isBold) paint.setFlags(paint.getFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                        if(isUnderline) paint.setFlags(paint.getFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        if(isStrike) paint.setFlags(paint.getFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        //just to get the boundary
                        Rect bounds = new Rect();
                        paint.getTextBounds(text, 0, text.length(), bounds);

                        //put new text on center
                        Widget textWidget = new TextWidget(bounds.width()
                                , bounds.height()
                                ,(screenSize.x / 2) - (bounds.width()/2)
                                ,(screenSize.y / 2) - (bounds.height()/2)
                                , text, paint);

                        currentSlide.addWidget(textWidget);
                        //Toast.makeText(this, "yey: " + test, Toast.LENGTH_SHORT).show();*/
                    } else if (requestCode == IMAGE_REQUEST_CODE) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        Log.i("Load Photo", picturePath);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        //options.inMutable = true;
                        options.inSampleSize = 4;

                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);

                        Paint paint = new Paint();
                        paint.setAntiAlias(true);
                        paint.setFilterBitmap(true);
                        paint.setDither(true);

                        //Log.i("Bitmap Dimension", "Width:" + bitmap.getWidth() + " Height: " + bitmap.getHeight());
                        PhotoWidget photoWidget = new PhotoWidget(bitmap.getWidth(), bitmap.getHeight()
                                ,(screenSize.x / 2) - (bitmap.getWidth()/2)
                                , (screenSize.y / 2) - (bitmap.getHeight()/2), bitmap, paint);
                        photoWidget.setValue(picturePath);
                        currentSlide.addWidget(photoWidget);
                        preview.invalidate();
                    }
                }

            }
        }.run();

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


    private void initializeUI(){
        previewContainer = findViewById(R.id.PPresentationPreviewContainer);

        //
        scaleSeekbar = findViewById(R.id.WidgetScale);
        //add
        addText = findViewById(R.id.AddTextButton);
        addImage = findViewById(R.id.AddImageButton);

        //add-remove
        addSlide = findViewById(R.id.AddSlide);
        removeSlide = findViewById(R.id.RemoveSlide);

        //next-prev
        nextSlide = findViewById(R.id.NextSlide);
        prevSlide = findViewById(R.id.PreviousSlide);

        //camera
        switchCamera = findViewById(R.id.SwitchCamera);
        toggleCamera = findViewById(R.id.ToggleCamera);

        //rotate
        rotateWidgetLeft = findViewById(R.id.RotateWidgetLeft);
        rotateWidgetRight = findViewById(R.id.RotateWidgetRight);

        //move
        moveWidgetForward = findViewById(R.id.MoveWidgetForward);
        moveWidgetBackward = findViewById(R.id.MoveWidgetBackward);

        //remove
        removeWidget = findViewById(R.id.RemoveWidget);

        //save widget
        savePresentation = findViewById(R.id.SavePresentation);

        showToggleUIWhenWidgetIsSelected(false);
    }

    private void initializeUIInteraction(){

        scaleSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(selectedWidget != null){
                    selectedWidget.setScale(progress * 0.5f);
                    preview.invalidate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //I just want to make the thing work now...
        //desperate situation needs desperate actions..
        //rotate
        rotateWidgetLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(selectedWidget != null) {
                    float rotation = selectedWidget.getRotation() - 1;
                    selectedWidget.setRotation(rotation);
                    preview.invalidate();
                }
                return true;
            }
        });

        rotateWidgetRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(selectedWidget != null) {
                    float rotation = selectedWidget.getRotation() + 1;
                    selectedWidget.setRotation(rotation);
                    preview.invalidate();
                }
                return true;
            }
        });

        //move up or down
        moveWidgetForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedWidget != null){
                    int curIndex = currentSlide.widgets.indexOf(selectedWidget);

                    if(curIndex + 1 <= currentSlide.widgets.size() - 1) {
                        Widget temp = currentSlide.widgets.get(curIndex + 1);
                        currentSlide.widgets.set(curIndex + 1, selectedWidget);
                        currentSlide.widgets.set(curIndex, temp);
                    }else{
                        Toast.makeText(NewPresentationActivity.this, "Already at front", Toast.LENGTH_SHORT).show();
                    }

                    preview.invalidate();
                }
            }
        });

        //move down
        moveWidgetBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedWidget != null){
                    int curIndex = currentSlide.widgets.indexOf(selectedWidget);

                    if(curIndex - 1 >= 0) {
                        Widget temp = currentSlide.widgets.get(curIndex - 1);
                        currentSlide.widgets.set(curIndex - 1, selectedWidget);
                        currentSlide.widgets.set(curIndex, temp);
                    }else{
                        Toast.makeText(NewPresentationActivity.this, "Already at back", Toast.LENGTH_SHORT).show();
                    }
                    preview.invalidate();
                }


            }
        });

        //delete widget
        removeWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedWidget != null){
                    currentSlide.widgets.remove(selectedWidget);
                    selectedWidget = null;
                    preview.invalidate();
                }

                showToggleUIWhenWidgetIsSelected(false);
            }
        });
        //widget add
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewPresentationActivity.this, NewTextActivity.class);
                startActivityForResult(i,TEXT_REQUEST_CODE);
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalStoragePermission extPermission = new ExternalStoragePermission(NewPresentationActivity.this);

                if(!extPermission.check()){
                    extPermission.request(NewPresentationActivity.this);
                }else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, IMAGE_REQUEST_CODE);
                }
            }
        });

        //add-remove
        addSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewPresentationActivity.this, "new slide", Toast.LENGTH_SHORT).show();
                currentSlide = new Slide();
                presentation.getSlides().add(currentSlide);
                preview.setSlide(currentSlide);
                slideCount++;
                slideIndex++;
                preview.invalidate();
            }
        });

        removeSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slideCount-1 <= 0){
                    Toast.makeText(NewPresentationActivity.this, "Atleast one slide is needed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NewPresentationActivity.this, "Slide removed", Toast.LENGTH_SHORT).show();
                    presentation.getSlides().remove(slideIndex);
                    slideCount = presentation.getSlides().size(); // get new size;

                    //move the slideIndex down by one; if it is greater than slideCount
                    if(slideIndex >= slideCount){
                        slideIndex--;
                    }

                    currentSlide = presentation.getSlides().get(slideIndex);
                    preview.setSlide(currentSlide);
                    preview.invalidate();
                }

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

        //toggle camera
        toggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appCamera.getCamera() != null){
                    appCamera.releaseCamera();
                    preview.setCamera(null);
                    previewContainer.removeAllViews();
                }else{
                    if(appCamera.isBackFacing()) appCamera.openCamera(0);
                    else appCamera.openCamera(1);

                    preview.setCamera(appCamera);

                    previewContainer.addView(preview);
                }
            }
        });

        //save
        savePresentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExternalStoragePermission esp = new ExternalStoragePermission(NewPresentationActivity.this);

                if(!esp.check()){
                    esp.request(NewPresentationActivity.this);
                }else{
                    fileHandler.saveFile("asd.iyp", presentation);
                }
            }
        });
        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                previewInteraction(v, event);
                return true;
            }
        });


    }

    private void previewInteraction(View v, MotionEvent event){

        //I implement the things I learn with game development here;
        int firstID = event.getPointerId(0);

        float fx = event.getX(event.findPointerIndex(firstID));
        float fy = event.getY(event.findPointerIndex(firstID));


        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //clean the methods association..
            //perhaps create a method move(motionevent)?
            if (currentSlide.widgets.size() > 0 && !isInMotionEvent) {
                for (Widget widget : currentSlide.widgets) {
                    if(widget.click(event)){
                        //if(lastSelectedWidget != null) lastSelectedWidget.willDrawBounds(false, false);
                        selectedWidget = widget;
                        showToggleUIWhenWidgetIsSelected(true);
                        preview.invalidate();
                        lastSelectedWidget = selectedWidget;
                        return;
                    }else {
                        if(selectedWidget != null) {
                            selectedWidget.willDrawBounds(false, false);
                            showToggleUIWhenWidgetIsSelected(false);
                            preview.invalidate();
                            selectedWidget = null;
                        }
                    }
                }
            }
        }


        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (selectedWidget != null) {
                isInMotionEvent = true;

                selectedWidget.setX((int)fx - (selectedWidget.getWidth()/2));
                selectedWidget.setY((int)fy - (selectedWidget.getHeight()/2));

                preview.invalidate();

            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            if (selectedWidget != null) {
                isInMotionEvent = false;
                //selectedWidget.willDrawBounds(false, false);
                //preview.invalidate();
                //selectedWidget = null;
            }

        }


        //re-implement this later
        /*else if(event.getPointerCount() >= 2){
            //Toast.makeText(this, "pointer = 2", Toast.LENGTH_SHORT).show();
            if(selectedWidget != null){
                MotionEvent.PointerCoords coord = new MotionEvent.PointerCoords();
                event.getPointerCoords(1, coord);

                float sx = coord.x;
                float sy = coord.y;

                float dotProd = ((selectedWidget.getCenter().x * sx) + (selectedWidget.getCenter().y * sy));
                float magCenter = (float)Math.sqrt(Math.pow(selectedWidget.getCenter().x,2) + Math.pow(selectedWidget.getCenter().y,2));
                float magSec = (float)Math.sqrt(Math.pow(sx,2) + Math.pow(sy,2));
                float rotation =  (float)Math.acos(dotProd / (magCenter * magSec));
                float newRotation = selectedWidget.getRotation() + ((float)Math.toDegrees(rotation) - selectedWidget.getRotation());

                selectedWidget.setRotation(newRotation);
                preview.invalidate();
            }
        }*/

    }


    private void showToggleUIWhenWidgetIsSelected(boolean show){

        if(show) {
            //rotate
            rotateWidgetLeft.setVisibility(View.VISIBLE);
            rotateWidgetRight.setVisibility(View.VISIBLE);

            //move
            moveWidgetForward.setVisibility(View.VISIBLE);
            moveWidgetBackward.setVisibility(View.VISIBLE);

            //remove
            removeWidget.setVisibility(View.VISIBLE);

            //
            scaleSeekbar.setVisibility(View.VISIBLE);
        }else{
            //rotate
            rotateWidgetLeft.setVisibility(View.GONE);
            rotateWidgetRight.setVisibility(View.GONE);

            //move
            moveWidgetForward.setVisibility(View.GONE);
            moveWidgetBackward.setVisibility(View.GONE);

            //remove
            removeWidget.setVisibility(View.GONE);

            //
            scaleSeekbar.setVisibility(View.GONE);
            //scaleSeekbar.setProgress((int)selectedWidget.getScale() * 2);

        }
    }
}
