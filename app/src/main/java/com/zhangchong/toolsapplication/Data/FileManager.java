package com.zhangchong.toolsapplication.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by Zhangchong on 2015/4/4.
 * 文件管理器
 */
public class FileManager {
    private String mCachePath = null;

    public  static FileManager instance;
    public static FileManager newInstance(Context context){
        if(instance == null)
            instance = new FileManager(context);
        return instance;
    }

    public static FileManager getInstance(){
        return instance;
    }

    private FileManager(Context context){
        if(hasSDCard()){
            mCachePath = context.getExternalCacheDir().getAbsolutePath();
        }else{
            mCachePath = context.getCacheDir().getAbsolutePath();
        }
    }

    public void onDestroy(){
    }

    private static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public String getFilePath(String fileName){
        return mCachePath + File.separator + fileName;
    }

}
