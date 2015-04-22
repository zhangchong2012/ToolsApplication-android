package com.zhangchong.libservice;

import android.app.Service;
import android.os.Binder;

/**
 * Created by Zhangchong on 2015/4/22.
 */
public class ToolsBinder extends Binder{
    private ToolsService mService;
    public ToolsBinder(ToolsService service){
        mService = service;
    }

    public ToolsService getService(){
        return mService;
    }

}
