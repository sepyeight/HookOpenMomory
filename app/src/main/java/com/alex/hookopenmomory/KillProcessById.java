package com.alex.hookopenmomory;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

public class KillProcessById {
    public static void restartAppFromPackagename(String packagename, Context context){
        ActivityManager am = (ActivityManager)context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = am.getRunningAppProcesses();
        int pid=-1;
        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess){
            if(amProcess.processName.equals(packagename)){
                pid=amProcess.pid;
                break;
            }

        }
        Log.i("MainActivity", pid+"");
        CommandExecution.execCommand("kill -9 "+ pid, true);
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent=new Intent();
            intent = packageManager.getLaunchIntentForPackage(packagename); //这里参数就是你要打开的app的包名
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("打开另外一个应用出错",e.getMessage());   //未打开，可能要打开的app没有安装，需要再此进行处理
        }
    }
}
