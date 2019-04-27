package com.nxiv.inlaypresentor.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class AudioPermission {

    private Context context;
    private final int MY_PERMISSIONS_REQUEST_AUDIO_RECORD = 3;

    public AudioPermission(Context context){
        this.context = context;
    }

    public boolean check(){
        boolean granted = false;

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED){
            granted = true;
        }

        return granted;
    }

    public void request(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.RECORD_AUDIO)) {
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_AUDIO_RECORD);
            }
        }
    }

}
