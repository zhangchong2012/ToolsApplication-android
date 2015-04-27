package com.zhangchong.weiblog.Manager;

import android.content.Context;

import com.zhangchong.weiblog.Manager.Data.Database.DatabaseManager;
import com.zhangchong.weiblog.Manager.Network.NetworkService;

/**
 * Created by Zhangchong on 2015/4/27.
 */
public class WeiblogManager {
    private static WeiblogManager manager;
    private Context mContext;
    private NetworkService mNetworkService;
    private DatabaseManager mDatabaseManager;

    public static WeiblogManager getManager(){
        return manager;
    }

    public static WeiblogManager newInstance(Context context){
        if(manager == null)
            manager = new WeiblogManager(context);
        return manager;
    }

    private WeiblogManager(Context context){
        mContext = context;
        mNetworkService = new NetworkService(context);
        mDatabaseManager = DatabaseManager.newInstance(context);

        mNetworkService.bindService();
    }

    public void onDestroy(){
        if(mNetworkService != null){
            mNetworkService.unBind();
            mNetworkService = null;
        }
    }

    /*database api*/

    /*network api*/
    public void requestLogin(String name, String pwd){
        mNetworkService.requestLogin(name, pwd);
    }

    public void requestRegister(){

    }
}
