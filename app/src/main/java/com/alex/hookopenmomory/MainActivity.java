package com.alex.hookopenmomory;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> selected;
    private  List<Appinfo> appinfos;
    private RecyclerView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.verifyStoragePermissions(this);

        appinfos = new ArrayList<>();
        getInstallAppList();

        listView = (RecyclerView) findViewById(R.id.recyclerView);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        final AppAdapter adapter = new AppAdapter(appinfos, getApplicationContext());
        listView.setAdapter(adapter);

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setSelection(position);
            }
        });
    }

    private void getInstallAppList() {
        try {
            if(getApiLevel() <= 23) {
                List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
                for (PackageInfo packageInfo : packageInfos) {
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        Appinfo info = new Appinfo();
                        info.setIcon(zoomDrawable(packageInfo.applicationInfo.loadIcon(getPackageManager()), DensityUtil.dip2px(this, 128), DensityUtil.dip2px(this, 128)));
                        info.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                        info.setAppPackage(packageInfo.packageName);
                        appinfos.add(info);
                    }
                }
            } else {
                Intent startupIntent = new Intent(Intent.ACTION_MAIN);
                startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                final PackageManager pm =  getPackageManager();
                List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
                for(ResolveInfo resolveInfo : activities) {
                    Appinfo appinfo = new Appinfo();
                    appinfo.setIcon(zoomDrawable(resolveInfo.loadIcon(pm), DensityUtil.dip2px(this, 128), DensityUtil.dip2px(this, 128)));
                    appinfo.setAppName(resolveInfo.loadLabel(pm).toString());
                    appinfo.setAppPackage(resolveInfo.activityInfo.packageName);
                    appinfos.add(appinfo);
                }
            }
//            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getApiLevel() {
        return Integer.parseInt(android.os.Build.VERSION.SDK);
    }



    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }


}
