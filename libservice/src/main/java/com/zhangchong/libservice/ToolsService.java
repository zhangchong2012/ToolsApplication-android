package com.zhangchong.libservice;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

import com.zhangchong.libnetwork.Core.Exception.NetException;
import com.zhangchong.libnetwork.Core.Request;
import com.zhangchong.libnetwork.Core.Response;
import com.zhangchong.libnetwork.NetworkManager;

public class ToolsService extends Service {
    private ToolsBinder mBinder;
    private NetworkManager mNetworkManager;
    public ToolsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(mBinder == null)
            mBinder = new ToolsBinder(this);
        mNetworkManager = NetworkManager.createNetworkManager(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNetworkManager.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }



    /************NetWork***********/
    public String startRquestAsync(Request<?> request){
        return mNetworkManager.startRquestAsync(request);
    }

    public Response<?> startRquestSync(Request<?> request) {
        Response<?> response = null;
        try{
            response = mNetworkManager.startRquestSync(request);
        }catch (NetException e){

        }
        return response;
    }
}
