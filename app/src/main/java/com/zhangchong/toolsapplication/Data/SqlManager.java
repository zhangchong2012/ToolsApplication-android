package com.zhangchong.toolsapplication.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zhangchong on 2015/4/4.
 */
public class SqlManager extends SQLiteOpenHelper{
    private static final String DB_NAME = "tools.db";
    private static final int DB_VERSION = 1;

    public SqlManager(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }


}
