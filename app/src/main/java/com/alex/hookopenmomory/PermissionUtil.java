package com.alex.hookopenmomory;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;

public class PermissionUtil {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static boolean isSDCardEXist(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void verifyStoragePermissions(MainActivity mainActivity) {

        if(!isSDCardEXist()){
            return;
        }

        int permission = ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(mainActivity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

    }
}
