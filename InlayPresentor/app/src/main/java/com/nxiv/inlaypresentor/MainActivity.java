package com.nxiv.inlaypresentor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nxiv.inlaypresentor.permissions.CameraPermission;
import com.nxiv.inlaypresentor.presentation.utility.PresentationFileHandler;

public class MainActivity extends AppCompatActivity {

    //User Interface
    Button present;
    Button newSlide;
    Button editSlide;

    //
    public static final String TRANSACTION_TYPE = "TRANSACTION_TYPE";
    public static final String PRESENTATION_FILENAME = "PRESENTATION_FILENAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        initializeUIInteraction();
    }

    private void initializeUI(){
        present = findViewById(R.id.PresentSideButton);
        newSlide = findViewById(R.id.NewSlideButton);
        editSlide = findViewById(R.id.EditSlideButton);
    }

    private void initializeUIInteraction(){
        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cp = new CameraPermission(MainActivity.this);
                if(!cp.check()) cp.request(MainActivity.this);


                Intent i = new Intent(MainActivity.this, PresentActivity.class);
                startActivity(i);
            }
        });

        newSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cp = new CameraPermission(MainActivity.this);
                if(!cp.check()) cp.request(MainActivity.this);

                Intent i = new Intent(MainActivity.this, NewPresentationActivity.class);
                i.putExtra(TRANSACTION_TYPE,"NEW_SLIDE");
                startActivity(i);
            }
        });

        editSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPermission cp = new CameraPermission(MainActivity.this);
                if(!cp.check()) cp.request(MainActivity.this);


                Intent i = new Intent(MainActivity.this, NewPresentationActivity.class);
                i.putExtra(TRANSACTION_TYPE,"EDIT_SLIDE");
                i.putExtra(PRESENTATION_FILENAME, "asd.iyp");
                startActivity(i);
            }
        });

    }

}
