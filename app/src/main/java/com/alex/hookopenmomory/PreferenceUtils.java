package com.alex.hookopenmomory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class PreferenceUtils {


    private static XSharedPreferences intance = null;
    private static final String PACKAGENAE = "com.alex.hookopenmomory";
    private static final String CONFIGFILENAME ="config";
    private static final String SELECTION = "selection";

    public static void write2Config(Context context, String packagename){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIGFILENAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SELECTION, packagename);
        editor.commit();
    }


    public static String readFromConfig(){
        if (intance == null){
            intance = new XSharedPreferences(PACKAGENAE, CONFIGFILENAME);
            intance.makeWorldReadable();
        }else {
            intance.reload();
        }
        String appname = intance.getString(SELECTION, "");
        return appname;
    }

    public static void write2Config2(String packagename){
        if (intance == null){
            intance = new XSharedPreferences(PACKAGENAE, CONFIGFILENAME);
            intance.makeWorldReadable();
        }else {
            intance.reload();
        }
        XSharedPreferences.Editor editor = intance.edit();
        editor.putString(SELECTION, packagename);
        editor.commit();

    }
}
