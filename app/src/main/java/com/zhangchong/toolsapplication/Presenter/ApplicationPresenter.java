package com.zhangchong.toolsapplication.Presenter;

import android.content.Context;

import com.zhangchong.toolsapplication.Data.FileManager;

/**
 * Created by Zhangchong on 2015/4/4.
 */
public class ApplicationPresenter {
    private FileManager mFileManager;

    public ApplicationPresenter(Context context){
        mFileManager = new FileManager(context);
    }

    public String getFilePath(String fileName){
        return mFileManager.getFilePath(fileName);
    }

    public void onDestroy(){
        mFileManager.onDestroy();
        mFileManager = null;
    }


}