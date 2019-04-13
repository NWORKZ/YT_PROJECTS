package com.nxiv.inlaypresentor.presentation.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.nxiv.inlaypresentor.permissions.ExternalStoragePermission;
import com.nxiv.inlaypresentor.presentation.Presentation;
import com.nxiv.inlaypresentor.presentation.Slide;
import com.nxiv.inlaypresentor.presentation.Widget;
import com.nxiv.inlaypresentor.presentation.widgets.PhotoWidget;
import com.nxiv.inlaypresentor.presentation.widgets.TextWidget;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class PresentationFileHandler {

    private Context context;
    private boolean willWriteToExternal = true;

    public PresentationFileHandler(Context context){
        this.context = context;

        if(!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(context, "External storage is not accessible", Toast.LENGTH_SHORT).show();
            willWriteToExternal = false;
        }

    }
    //save file
    public void saveFile(String filename, Presentation presentation){
        if(willWriteToExternal) {
            File file = getPrivateDocumentStorageDir(context, filename);
            //Log.i("test", file.getPath());

            String content = "";
            StringBuilder stringBuilder = new StringBuilder();

            for (Slide slide : presentation.getSlides()) {
                stringBuilder.append("-slide:");
                for (Widget widget : slide.widgets) {
                    Log.i("saveWT", widget.getType());
                    stringBuilder.append("-widget:"+ widget.getType());
                    stringBuilder.append("," + widget.getX() + "," + widget.getY() + "," + widget.getWidth() + "," + widget.getHeight());
                    stringBuilder.append("," + widget.getRotation() + "," + widget.getScale() + "," + widget.getValue());
                    stringBuilder.append("," + widget.getPaint().getColor() + "," + widget.getPaint().getTextSize());

                    if((widget.getPaint().getFlags() & Paint.FAKE_BOLD_TEXT_FLAG ) > 0) stringBuilder.append("," + "true");
                    else stringBuilder.append("," + "false");

                    if((widget.getPaint().getFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) stringBuilder.append("," + "true");
                    else stringBuilder.append("," + "false");

                    if((widget.getPaint().getFlags() & Paint.UNDERLINE_TEXT_FLAG) > 0) stringBuilder.append("," + "true");
                    else stringBuilder.append("," + "false");
                }
            }

            content = stringBuilder.toString();
            Log.i("context", content);
            try {
                FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(content.getBytes());
                fos.close();
            } catch (IOException e) {
                Log.e("FOS", "FOS FAIL");
            }
        }else{
            Toast.makeText(context, "Saving File Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public Presentation getPresentation(String filename) {
        File file = getPrivateDocumentStorageDir(context, filename);
        Presentation presentation = null;

        try {
            FileInputStream fis = context.openFileInput(filename);
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));

            String line;
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null){
                sb.append(line);
            }

            String fileData = sb.toString();
            //Log.i("filedata", fileData);
            presentation = new Presentation();
            String[] slideDefs = fileData.split("-slide:");
            int slideCount = 0;
            for(String slideDef : slideDefs){

                if(slideCount != 0) {
                    Slide slide = new Slide();

                    String[] widgetDefs = slideDef.split("-widget:");

                    int widgetCount = 0;

                    for (String widgetDef : widgetDefs) {

                        String[] widgetProp = widgetDef.split(",");

                        Widget widget;

                        if (widgetCount != 0) {
                            Log.i("loadWT", widgetCount + "");
                            if (widgetProp[0].equals("PHOTO_WIDGET")) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                //options.inMutable = true;
                                options.inSampleSize = 4;

                                Bitmap bitmap = BitmapFactory.decodeFile(widgetProp[7], options);

                                Paint paint = new Paint();
                                paint.setAntiAlias(true);
                                paint.setFilterBitmap(true);
                                paint.setDither(true);

                                widget = new PhotoWidget(Integer.parseInt(widgetProp[3]), Integer.parseInt(widgetProp[4])
                                        , Integer.parseInt(widgetProp[1]), Integer.parseInt(widgetProp[2]), bitmap, paint);
                                widget.setRotation(Float.parseFloat(widgetProp[5]));
                                widget.setScale(Float.parseFloat(widgetProp[6]));
                            } else {

                                Paint paint = new Paint();
                                paint.setStyle(Paint.Style.FILL);
                                paint.setColor(Integer.parseInt(widgetProp[8]));
                                paint.setTextSize(Float.parseFloat(widgetProp[9]));
                                //paint.setTextScaleX(1.0f);//set to default first

                                if (Boolean.parseBoolean(widgetProp[10]))
                                    paint.setFlags(paint.getFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                                if (Boolean.parseBoolean(widgetProp[11]))
                                    paint.setFlags(paint.getFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                if (Boolean.parseBoolean(widgetProp[12]))
                                    paint.setFlags(paint.getFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                widget = new TextWidget(Integer.parseInt(widgetProp[3]), Integer.parseInt(widgetProp[4])
                                        , Integer.parseInt(widgetProp[1]), Integer.parseInt(widgetProp[2]), widgetProp[7], paint);

                                widget.setRotation(Float.parseFloat(widgetProp[5]));
                                widget.setScale(Float.parseFloat(widgetProp[6]));
                            }

                            slide.addWidget(widget);
                        }

                        widgetCount++;
                    }

                    presentation.getSlides().add(slide);
                }

                slideCount++;
            }

            dis.close();
        }catch (IOException e){
            Log.e("FIS", "FIS FAIL");
        }

        return presentation;
    }
    /*//save video file
    public void saveVideoFile(String filename){

    }*/

    //external storage
    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //for saving the video output file
    public static File getPublicAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), albumName);
        if (!file.mkdirs()) {
            Log.e("EXTERNAL_STORAGE", "Directory not created");
        }
        return file;
    }

    public File getPrivateDocumentStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), albumName);
        if (!file.mkdirs()) {
            Log.e("EXTERNAL_STORAGE", "Directory not created");
            //Toast.makeText(context, "Error saving presentation", Toast.LENGTH_SHORT).show();
        }
        return file;
    }


}
