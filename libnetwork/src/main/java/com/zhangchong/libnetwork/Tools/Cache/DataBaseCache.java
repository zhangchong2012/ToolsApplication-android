package com.zhangchong.libnetwork.Tools.Cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.zhangchong.libnetwork.Core.Cache;
import com.zhangchong.libnetwork.Tools.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

public class DataBaseCache implements Cache {
    private Context mContext;
    //20m的内容大小
    private final LruCache<String, Cache.Entry> mEntrys = new LruCache<String, Cache.Entry>(20);
//    private final HashMap<String, Cache.Entry> mPenddingWriteEntrys = new HashMap<String, Cache.Entry>();

    public DataBaseCache(Context context) {
        mContext = context;
    }

    @Override
    public Entry get(String key) {
        Cache.Entry entry = null;
        if(TextUtils.isEmpty(key))
            return entry;
        synchronized (mEntrys) {
            entry = mEntrys.get(key);
        }
        if (entry != null) {
            return entry;
        }
        entry = queryEntryFromContentProvider(key);
        return entry;
    }


    @Override
    public void put(String key, Entry entry) {
        //缓存记录和数据一起保存，以防止缓存记录保存了，但数据没有的情况。

        //DataManager.getInstance().getDatabaseDataManager().updateCacheEntry(key, entry);
        //mEntrys.put(key, entry);
//        synchronized (mPenddingWriteEntrys) {
//            mPenddingWriteEntrys.put(key, entry);
//        }
        if (entry != null) {
            updateOrInsertEntry(key, entry);
            synchronized (mEntrys) {
                mEntrys.put(key, entry);
            }
        }
    }

//    public Entry flushCache(String key) {
//        return flushCache(key, DatabaseHelper.TYPE_NORMAL_URL);
//    }

    public Entry flushCache(String key, int type) {
        Entry entry = null;
//        synchronized (mPenddingWriteEntrys) {
//            entry = mPenddingWriteEntrys.remove(key);
//        }
        if (entry != null) {
//            DataManager.getInstance().getDatabaseDataManager().updateCacheEntry(key, entry, type);
            synchronized (mEntrys) {
                mEntrys.put(key, entry);
            }
        }
        return entry;
    }

    @Override
    public void initialize() {
        //Nothing. create db
    }

    @Override
    public void invalidate(String key, boolean fullExpire) {
        //这个函数其实Volley没有调用。
//        Entry entry = get(key);
//        if (entry != null) {
//            entry.softTtl = 0;
//            if (fullExpire) {
//                entry.ttl = 0;
//            }
//            put(key, entry);
//            DataManager.getInstance().getDatabaseDataManager().updateCacheTtl(key, entry.softTtl, entry.ttl);
//        }
    }

    @Override
    public void remove(String key) {
        //这个函数其实Volley没有调用。
        synchronized (mEntrys) {
            mEntrys.remove(key);
        }
//        synchronized (mPenddingWriteEntrys) {
//            mPenddingWriteEntrys.remove(key);
//        }
//        DataManager.getInstance().getDatabaseDataManager().deleteCacheEntry(key);
    }

    @Override
    public void clear() {
        synchronized (mEntrys) {
            mEntrys.evictAll();
            clearEntryDatabases();
        }
//        synchronized (mPenddingWriteEntrys) {
//            mPenddingWriteEntrys.clear();
//        }
//        DataManager.getInstance().getDatabaseDataManager().deleteAllCacheEntry();
    }



    private Entry queryEntryFromContentProvider(String key){
        Entry entry = null;
        Cursor cursor = null;
        try{
            String selection = CacheBean.Columns.COLUMN_ENTRY_CACHE_URL +" = ?";
            String[] selectionArgs = new String[]{key};
            cursor= mContext.getContentResolver().query(CacheBean.CONTENT_URI, null, selection, selectionArgs, null);
            //parse cursor
            while (cursor.moveToNext()){
                entry = new Entry();
                entry.etag = cursor.getString(cursor.getColumnIndex(CacheBean.Columns.COLUMN_ENTRY_CACHE_ETAG));
                entry.serverDate = cursor.getLong(cursor.getColumnIndex(CacheBean.Columns.COLUMN_ENTRY_CACHE_SERVER_DATE));
                entry.ttl = cursor.getLong(cursor.getColumnIndex(CacheBean.Columns.COLUMN_ENTRY_CACHE_TTL));
                entry.softTtl = cursor.getLong(cursor.getColumnIndex(CacheBean.Columns.COLUMN_ENTRY_CACHE_SOFT_TTL));
                String responseHeaderStr = cursor.getString(cursor.getColumnIndex(CacheBean.Columns.COLUMN_ENTRY_CACHE_RESPONSE_HEADER));
                String responseHeaderInfo = cursor.getString(cursor.getColumnIndex(CacheBean.Columns.COLUMN_ENTRY_CACHE_HEADER_LENGTH_INFO));
                entry.responseHeaders = parseHeaderInfo(responseHeaderInfo, responseHeaderStr);
            }
        }catch (Exception e){
            remove(key);
        }finally {
            if(cursor != null)
                cursor.close();
        }

        return entry;
    }

    private void updateOrInsertEntry(String key, Entry entry){
        String selection = CacheBean.Columns.COLUMN_ENTRY_CACHE_URL +" = ?";
        String[] selectionArgs = new String[]{key};
        ContentValues values = CacheBean.convertToContentValues(key, entry);
        if(values == null)
            return;
        int id = mContext.getContentResolver().update(CacheBean.CONTENT_URI, values, selection, selectionArgs);
    }


    private void clearEntryDatabases(){
        mContext.getContentResolver().delete(CacheBean.CONTENT_URI, null, null);
    }

    private Map<String, String> parseHeaderInfo(String headerInfo, String headerBody){
        Map<String, String> map = null;
        if(TextUtils.isEmpty(headerInfo) || TextUtils.isEmpty(headerBody))
            return map;
        String[] headerLength = headerInfo.split(",");
        int charIndex = 0;
        String key = null;
        String value = null;
        map = new HashMap<String, String>();
        for (String length : headerLength) {
            int lengthInt = Integer.parseInt(length);
            String subString = headerBody.substring(charIndex, charIndex + lengthInt);
            if (key == null) {
                key = subString;
            } else {
                value = subString;
            }
            if (key != null && value != null) {
                map.put(key, value);
                key = value = null;
            }
            charIndex += lengthInt + 1;
        }
        if (map.containsKey("Last-Modified")) {
            map.put("Last-Modified", String.valueOf(HttpHeaderParser.parseDateAsEpoch(map.get("Last-Modified"))));
        }

        return map;
    }
}
