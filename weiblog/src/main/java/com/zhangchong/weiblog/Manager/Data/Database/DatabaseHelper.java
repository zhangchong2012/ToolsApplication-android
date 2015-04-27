package com.zhangchong.weiblog.Manager.Data.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhangchong.libdao.DAO.DaoEntrySchema;

/**
 * Created by Zhangchong on 2015/4/27.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weiblog.db";
    private static final int DB_CURRENT_VERSION = 1;
    public static DatabaseHelper mHelper;


    private static final DaoEntrySchema[] initTables = {
    };

    public static DatabaseHelper getInstance() {
        return mHelper;
    }

    public static DatabaseHelper newInstance(Context context){
        if(mHelper == null)
            mHelper = new DatabaseHelper(context);
        return mHelper;
    }

    private DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DB_CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, DB_CURRENT_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        createTablesPlus(db);
    }

    private void createTablesPlus(SQLiteDatabase db) {
        for (DaoEntrySchema schema : initTables) {
            schema.dropTables(db);
            schema.createTables(db);
        }
    }

}
