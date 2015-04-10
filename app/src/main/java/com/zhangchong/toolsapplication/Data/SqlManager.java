package com.zhangchong.toolsapplication.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhangchong.toolsapplication.Data.Bean.ExcelCellBean;
import com.zhangchong.toolsapplication.Data.Bean.ExcelSheetBean;
import com.zhangchong.toolsapplication.Data.Bean.FileBean;
import com.zhangchong.toolsapplication.Data.Bean.QRBean;
import com.zhangchong.toolsapplication.Data.DAO.DaoEntrySchema;

/**
 * Created by Zhangchong on 2015/4/4.
 */
public class SqlManager extends SQLiteOpenHelper{
    private static final String DB_NAME = "tools.db";
    private static final int DB_VERSION = 1;

    private static final DaoEntrySchema[] initTables = {
            ExcelSheetBean.schema,
            ExcelCellBean.schema,
            FileBean.schema,
            QRBean.schema
    };

    public SqlManager(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

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
        for (DaoEntrySchema schema : initTables) {
            schema.dropTables(db);
            schema.createTables(db);
        }
    }


}
