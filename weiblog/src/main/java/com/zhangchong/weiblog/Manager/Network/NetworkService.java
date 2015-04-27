package com.zhangchong.weiblog.Manager.Network;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.zhangchong.libnetwork.Core.Exception.NetException;
import com.zhangchong.libnetwork.Core.Response;
import com.zhangchong.libservice.ToolsBinder;
import com.zhangchong.libservice.ToolsService;
import com.zhangchong.libutils.LogHelper;
import com.zhangchong.weiblog.Manager.Network.Api.RequestLogin;
import com.zhangchong.weiblog.Manager.Network.Bean.LoginBean;

/**
 * Created by Zhangchong on 2015/4/22.
 */
public class NetworkService implements ServiceConnection {
    private ToolsService mService;
    private Context mContext;
    private boolean mIsBind;

    public NetworkService(Context context) {
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


    public void requestLogin(String name, String pwd){
        RequestLogin requestLogin = new RequestLogin(name, pwd, new Response.Listener<LoginBean>() {
            @Override
            public void onResponse(LoginBean response) {
                LogHelper.logD(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(NetException error) {
                LogHelper.logD(error.getMessage());
            }
        });
        mService.startRquestAsync(requestLogin);
    }
}
