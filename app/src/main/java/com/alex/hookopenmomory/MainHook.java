package com.alex.hookopenmomory;

import android.content.Context;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


// art模式下的Xposed Hook
public class MainHook implements IXposedHookLoadPackage {

    private static final String TAG = "DEX_DUMP";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        String pkgName = PreferenceUtils.readFromConfig();
        if(!pkgName.equals(loadPackageParam.packageName)){
            return;
        }

        XposedBridge.log("对" + loadPackageParam.packageName + "进行处理");
        try {
            System.load("/data/data/com.alex.hookopenmomory/lib/libhook.so");
        }catch (Exception e){
            XposedBridge.log("erroroooooo");
        }

        // 对Android系统类android.app.Application的attach函数进行art模式下的Hook操作
        XposedHelpers.findAndHookMethod("android.app.Application", loadPackageParam.classLoader, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                // 在类android.app.Application的attach函数调用之前进行dex文件的内存dump操作
                XposedBridge.log("开始dump了...");
                Dumper.dump();
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });
    }



}