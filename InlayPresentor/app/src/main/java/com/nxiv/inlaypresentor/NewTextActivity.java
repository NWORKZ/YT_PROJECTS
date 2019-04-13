package com.nxiv.inlaypresentor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nxiv.inlaypresentor.presentation.widgets.TextWidget;

import org.w3c.dom.Text;

public class NewTextActivity extends AppCompatActivity {


    //
    private boolean underline, strike, bold;
    private int color;
    //UI
    private EditText textInput;
    private SeekBar fontSizeInput;
    private TextView textPreview;

    private ImageButton toggleBold, toggleStrike, toggleUnderline;

    private SeekBar redSeekBar, greenSeekBar, blueSeekbar;

    private Button doneButton, cancelButton;

    private TextView hexColorPreview;

    //activity extra tags
    public static final String TEXT_EXTRA = "TEXT";
    public static final String TEXT_SIZE_EXTRA = "TEXT_SIZE";
    public static final String TEXT_BOLD_EXTRA = "TEXT_BOLD";
    public static final String TEXT_STRIKE_EXTRA = "TEXT_STRIKE";
    public static final String TEXT_UNDERLINE_EXTRA = "TEXT_UNDERLINE";
    public static final String TEXT_COLOR_EXTRA = "TEXT_COLOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_text);

        //preview = new DrawingPreview(this);

        initializeUI();
        initializeUIInteraction();
    }


    private void initializeUI(){
        doneButton = findViewById(R.id.TextDoneButton);
        cancelButton = findViewById(R.id.TextCancelButton);

        //
        hexColorPreview = findViewById(R.id.HexColorPreview);

        //
        textInput = findViewById(R.id.TextValue);
        textPreview = findViewById(R.id.TextPreview);
        fontSizeInput = findViewById(R.id.FontSizeSeekBar);

        fontSizeInput.setProgress(24);
        textPreview.setTextSize(TypedValue.COMPLEX_UNIT_PX,24);

        //text styles
        toggleBold = findViewById(R.id.TextBoldValue);
        toggleStrike = findViewById(R.id.TextStrikeValue);
        toggleUnderline = findViewById(R.id.TextUnderlineValue);

        //seekbar
        redSeekBar = findViewById(R.id.RedSeekBar);
        redSeekBar.setProgress(redSeekBar.getMax());

        greenSeekBar = findViewById(R.id.GreenSeekBar);
        greenSeekBar.setProgress(greenSeekBar.getMax());

        blueSeekbar = findViewById(R.id.BlueSeekBar);
        blueSeekbar.setProgress(blueSeekbar.getMax());

        //data defaults
        color = Color.rgb(redSeekBar.getProgress(), greenSeekBar.getProgress(), blueSeekbar.getProgress());
    }

    private void initializeUIInteraction(){

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textPreview.setText(textInput.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fontSizeInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textPreview.setTextSize(TypedValue.COMPLEX_UNIT_PX, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //text style
        toggleBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bold) {
                    textPreview.setPaintFlags(textPreview.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                    bold = true;
                }
                else {
                    textPreview.setPaintFlags(textPreview.getPaintFlags() ^Paint.FAKE_BOLD_TEXT_FLAG );
                    bold = false;
                }
            }
        });

        toggleUnderline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!underline) {
                    textPreview.setPaintFlags(textPreview.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    underline = true;
                }else{
                    textPreview.setPaintFlags(textPreview.getPaintFlags() ^Paint.UNDERLINE_TEXT_FLAG);
                    underline = false;
                }
            }
        });

        toggleStrike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!strike) {
                    textPreview.setPaintFlags(textPreview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    strike = true;
                }else{
                    textPreview.setPaintFlags(textPreview.getPaintFlags() ^Paint.STRIKE_THRU_TEXT_FLAG);
                    strike = false;
                }
            }
        });


        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                color = Color.rgb(progress, greenSeekBar.getProgress(), blueSeekbar.getProgress());
                textPreview.setTextColor(color);
                Integer hexColor = textPreview.getCurrentHintTextColor();
                hexColorPreview.setText("#" + hexColor.toHexString(color));
                hexColorPreview.setTextColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                color = Color.rgb(redSeekBar.getProgress(), progress, blueSeekbar.getProgress());
                textPreview.setTextColor(color);
                Integer hexColor = textPreview.getCurrentHintTextColor();
                hexColorPreview.setText("#" + hexColor.toHexString(color));
                hexColorPreview.setTextColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                color = Color.rgb(redSeekBar.getProgress(), greenSeekBar.getProgress(), progress);
                textPreview.setTextColor(color);
                Integer hexColor = textPreview.getCurrentHintTextColor();
                hexColorPreview.setText("#" + hexColor.toHexString(color));
                hexColorPreview.setTextColor(color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();

                //this is it for now
                i.putExtra(TEXT_EXTRA, textInput.getText().toString()); //text value
                i.putExtra(TEXT_SIZE_EXTRA, fontSizeInput.getProgress());//font size
                i.putExtra(TEXT_COLOR_EXTRA,color);
                i.putExtra(TEXT_BOLD_EXTRA, bold);
                i.putExtra(TEXT_UNDERLINE_EXTRA, underline);
                i.putExtra(TEXT_STRIKE_EXTRA, strike);

                //add fonts and other things here later
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

    }

}
