package com.zhangchong.libdao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.zhangchong.libdao.DAO.DaoEntrySchema;

/**
 * Created by Zhangchong on 2015/4/4.
 */
public abstract class SqlManager extends SQLiteOpenHelper{
    private static final String DB_NAME = "tools.db";
    private static final int DB_VERSION = 1;


    public SqlManager(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    public abstract DaoEntrySchema[] getTables();

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构
        onUpgrade(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion > newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
            oldVersion = 0;
        }

        if(oldVersion < 1){
            createBasicDB(db);
            oldVersion = 1;
        }

        //TODO
        if(oldVersion < 2){

        }
    }

    private void createBasicDB(SQLiteDatabase db){
        for (DaoEntrySchema schema : getTables()) {
            schema.dropTables(db);
            schema.createTables(db);
        }
    }


}
