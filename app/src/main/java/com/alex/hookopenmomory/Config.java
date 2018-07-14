package com.alex.hookopenmomory;

import android.os.Environment;
import android.util.Log;
import org.json.JSONArray;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final String FILENAME = Environment.getExternalStorageDirectory().getPath() +File.separator + "dumpdex.js";
    private  static  final String TAG = "selectedapp";

    private static void writeConfig(String s){
        try{
            FileOutputStream fos = new FileOutputStream(FILENAME);
            fos.write(s.getBytes());
            fos.flush();
            fos.close();
            Log.d(TAG, "write file");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void addOne(String name) {
        deleteFile();
        List<String> ori = getConfig();
        if(ori == null) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(name);
            writeConfig(jsonArray.toString());
        }else {
            ori.add(name);
            JSONArray jsonArray = new JSONArray();
            for(String o : ori) {
                jsonArray.put(o);
            }
            writeConfig(jsonArray.toString());
        }
    }

    public static void deleteFile(){
        new File(FILENAME).delete();
    }

    public static List<String> getConfig() {
        File file = new File(FILENAME);
        if(file.exists() && file.canRead()){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line = br.readLine();
                JSONArray jsonArray = new JSONArray(line);
                List<String> apps = new ArrayList<>();
                for(int i=0; i<jsonArray.length(); i++){
                    apps.add(jsonArray.getString(i));
                }
                br.close();
                Log.d(TAG, "需要hook的列表:"+line);
                return apps;
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            try{
                if(!file.exists()){
                    file.createNewFile();
                }
                if (!file.canRead()){
                    Log.d(TAG, "文件没有读取权限");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }

    public static boolean contains(List<String> lists, String name) {
        if(lists == null) {
            return false;
        }
        for(String l : lists) {
            if(l.equals(name)) {
                return true;
            }
        }
        return false;
    }


}
