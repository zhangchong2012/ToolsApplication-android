package com.zhangchong.toolsapplication.Presenter;

import android.content.Context;

import com.zhangchong.libutils.FileManager;

/**
 * Created by Zhangchong on 2015/4/4.
 */
public class ApplicationPresenter {
    private FileManager mFileManager;

    public ApplicationPresenter(Context context){
        mFileManager = FileManager.newInstance(context);
    }

    public String getFilePath(String fileName){
        return mFileManager.getFilePath(fileName);
    }

    public void onDestroy(){
        mFileManager.onDestroy();
        mFileManager = null;
    }


}
