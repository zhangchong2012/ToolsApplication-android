package com.zhangchong.weiblog.Manager.Data.Database;

import android.content.Context;

/**
 * Created by Zhangchong on 2015/4/27.
 */
public class DatabaseManager {
    private static DatabaseManager manager;
    private DatabaseHelper mDatabaseHelper;
    public static DatabaseManager getInstance(){
        return manager;
    }

    public static DatabaseManager newInstance(Context context){
        if(manager == null)
            manager = new DatabaseManager(context);
        return manager;
    }

    private DatabaseManager(Context context){
        mDatabaseHelper = DatabaseHelper.newInstance(context);
    }
}
