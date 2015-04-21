package com.zhangchong.toolsapplication.Presenter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.zhangchong.libdao.SqlManager;
import com.zhangchong.libnetwork.Tools.Cache.CacheBean;
import com.zhangchong.toolsapplication.Data.Bean.ExcelCellBean;
import com.zhangchong.toolsapplication.Data.Bean.ExcelSheetBean;
import com.zhangchong.libdao.DAO.DaoEntrySchema;
import com.zhangchong.toolsapplication.Data.Bean.FileBean;
import com.zhangchong.toolsapplication.Data.Bean.QRBean;

public class ToolsContentProvider extends ContentProvider {
    private SqlManager mSqlManager;

    private static final DaoEntrySchema[] initTables = {
            ExcelSheetBean.schema,
            ExcelCellBean.schema,
            FileBean.schema,
            QRBean.schema,
            CacheBean.schema
    };
    @Override
    public boolean onCreate() {
        mSqlManager = new SqlManager(getContext()){
            @Override
            public DaoEntrySchema[] getTables() {
                return initTables;
            }
        };
        return mSqlManager != null;
    }

    @Override
    public String getType(Uri uri) {
        //TODO explicit intent
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = ToolsUri.URI_MATCHER.match(uri);
        long id = -1;
        Uri rowUri = null;
        switch (match){
            case ToolsUri.ExcelUri.EXCEL_FILE:
                //TODO twice object convert to values, need optimize
                id = ExcelSheetBean.schema.insertOrReplace(mSqlManager.getWritableDatabase(),
                        ToolsUri.ExcelFileColumn.parseContentValues(values));
                rowUri = ContentUris.withAppendedId(uri, id);
                break;
            case ToolsUri.ExcelUri.EXCEL_CELL:
                id = ExcelCellBean.schema.insertOrReplace(mSqlManager.getWritableDatabase(),
                        ExcelCellBean.schema.valuesToObject(values, new ExcelCellBean()));
                rowUri = ContentUris.withAppendedId(uri, id);
            case ToolsUri.NETWORK_CACHE:
            case ToolsUri.NETWORK_CACHE_ID: {
                id = insertOrReplace(mSqlManager.getWritableDatabase(), values, CacheBean.schema);
                rowUri = ContentUris.withAppendedId(uri, id);
            }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (id > 0){
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        return rowUri;
    }

    private long insertOrReplace(SQLiteDatabase db, ContentValues values, DaoEntrySchema schema) {
        if (values == null)
            return -1;

        if (values.containsKey(BaseColumns._ID) &&
                values.getAsInteger(BaseColumns._ID) == 0) {
            values.remove("_id");
        }
        long id = db.replace(schema.getTableName() , "_id", values);
        return id;
    }


    private void insertOrReplace(SQLiteDatabase db, ContentValues[] values, DaoEntrySchema schema) {
        if (values == null)
            return;

        for (ContentValues entry : values) {
            if(entry == null)
                continue;
            if (entry.containsKey(BaseColumns._ID) &&
                    entry.getAsInteger(BaseColumns._ID) == 0) {
                entry.remove("_id");
            }
            long id = db.replace(schema.getTableName() , "_id", entry);
            entry.put(BaseColumns._ID, id);
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int match = ToolsUri.URI_MATCHER.match(uri);
        int count = 0;
        SQLiteDatabase db = mSqlManager.getWritableDatabase();
        switch (match){
            case ToolsUri.ExcelUri.EXCEL_FILE: {
                db.beginTransaction();
                try {
                    insertOrReplace(db, values, ExcelSheetBean.schema);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
                break;
            case ToolsUri.ExcelUri.EXCEL_CELL: {
                db.beginTransaction();
                try {
                    insertOrReplace(db, values, ExcelCellBean.schema);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
            case ToolsUri.NETWORK_CACHE:
            case ToolsUri.NETWORK_CACHE_ID:{
                db.beginTransaction();
                try {
                    insertOrReplace(db, values, ExcelCellBean.schema);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = ToolsUri.URI_MATCHER.match(uri);
        int count = 0;
        switch (match){
            case ToolsUri.ExcelUri.EXCEL_FILE:
                count = ExcelSheetBean.schema.delete(mSqlManager.getWritableDatabase(), selection, selectionArgs);
                break;
            case ToolsUri.ExcelUri.EXCEL_FILE_ID: {
                long id = ContentUris.parseId(uri);
                count = ExcelSheetBean.schema.deleteWithId(mSqlManager.getWritableDatabase(), id);
            }
                break;
            case ToolsUri.ExcelUri.EXCEL_CELL:
                count = ExcelCellBean.schema.delete(mSqlManager.getWritableDatabase(), selection, selectionArgs);
                break;
            case ToolsUri.ExcelUri.EXCEL_CELL_ID:{
                long id = ContentUris.parseId(uri);
                count = ExcelCellBean.schema.deleteWithId(mSqlManager.getWritableDatabase(), id);
            }
            break;
            case ToolsUri.NETWORK_CACHE:{
                ExcelCellBean.schema.delete(mSqlManager.getWritableDatabase(), selection, selectionArgs);
            }
            case ToolsUri.NETWORK_CACHE_ID:{
                long id = ContentUris.parseId(uri);
                count = ExcelCellBean.schema.deleteWithId(mSqlManager.getWritableDatabase(), id);

            }
            break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = ToolsUri.URI_MATCHER.match(uri);
        switch (match){
            case ToolsUri.ExcelUri.EXCEL_FILE:
                return ExcelSheetBean.schema.query(mSqlManager.getReadableDatabase(), selection, selectionArgs, null, null, sortOrder);
            case ToolsUri.ExcelUri.EXCEL_FILE_ID:
                return ExcelSheetBean.schema.query(mSqlManager.getReadableDatabase(), selection, selectionArgs, null, null, sortOrder);
            case ToolsUri.ExcelUri.EXCEL_CELL:
                return ExcelCellBean.schema.query(mSqlManager.getReadableDatabase(), selection, selectionArgs, null, null, sortOrder);
            case ToolsUri.ExcelUri.EXCEL_CELL_ID:
                return ExcelCellBean.schema.query(mSqlManager.getReadableDatabase(), selection, selectionArgs, null, null, sortOrder);
            case ToolsUri.NETWORK_CACHE:
            case ToolsUri.NETWORK_CACHE_ID:
                return CacheBean.schema.query(mSqlManager.getReadableDatabase(), selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = ToolsUri.URI_MATCHER.match(uri);
        int count = 0;
        switch (match){
            case ToolsUri.ExcelUri.EXCEL_FILE:
                count = ExcelSheetBean.schema.update(mSqlManager.getWritableDatabase(), values, selection, selectionArgs);
                break;
            case ToolsUri.ExcelUri.EXCEL_FILE_ID: {
                count = ExcelSheetBean.schema.update(mSqlManager.getWritableDatabase(), values, selection, selectionArgs);
            }
            break;
            case ToolsUri.ExcelUri.EXCEL_CELL:
                count = ExcelCellBean.schema.update(mSqlManager.getWritableDatabase(), values, selection, selectionArgs);
                break;
            case ToolsUri.ExcelUri.EXCEL_CELL_ID:{
                count = ExcelCellBean.schema.update(mSqlManager.getWritableDatabase(), values, selection, selectionArgs);
                break;
            }
            case ToolsUri.NETWORK_CACHE:{
                count = CacheBean.schema.update(mSqlManager.getWritableDatabase(), values, selection, selectionArgs);
            }
            break;
            case ToolsUri.NETWORK_CACHE_ID: {
                count = CacheBean.schema.update(mSqlManager.getWritableDatabase(), values, selection, selectionArgs);
            }
            break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }


}
