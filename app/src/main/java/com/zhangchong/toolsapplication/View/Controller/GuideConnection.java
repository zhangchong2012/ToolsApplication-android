package com.zhangchong.toolsapplication.View.Controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.zhangchong.libservice.ToolsBinder;
import com.zhangchong.libservice.ToolsService;

/**
 * Created by Zhangchong on 2015/4/22.
 */
public class GuideConnection implements ServiceConnection {
    private ToolsService mService;
    private Context mContext;
    private boolean mIsBind;

    public GuideConnection(Context context) {
        mContext = context;
    }

    public void bindService() {
        Intent intent = new Intent(mContext, ToolsService.class);
        mContext.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    public void unBind() {
        if (mIsBind == true) {
            mContext.unbindService(this);
            mIsBind = false;
        }
    }

    public ToolsService getService() {
        return mService;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ToolsBinder binder = (ToolsBinder) service;
        mService = binder.getService();
        mIsBind = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
        mIsBind = false;
    }
}
