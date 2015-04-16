package com.zhangchong.libnetwork.Tools.Cache;

import android.content.Context;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.zhangchong.libnetwork.Core.Cache;

import java.util.HashMap;

public class NetworkCache implements Cache {
    private Context mContext;
    //20m的内容大小
    private final LruCache<String, Cache.Entry> mEntrys = new LruCache<String, Cache.Entry>(20);
    private final HashMap<String, Cache.Entry> mPenddingWriteEntrys = new HashMap<String, Cache.Entry>();

    public NetworkCache(Context context) {
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
        //如果在刚刚下载但还未保存的缓存中。
        synchronized (mPenddingWriteEntrys) {
            Cache.Entry peddingEntry = mPenddingWriteEntrys.get(key);
            if (peddingEntry != null && peddingEntry.data != null) {
                entry = peddingEntry;
            }
        }
        if (entry != null) {
            return entry;
        }
//        entry = DataManager.getInstance().getDatabaseDataManager().queryCacheEntry(key);
        return entry;
    }

    @Override
    public void put(String key, Entry entry) {
        //缓存记录和数据一起保存，以防止缓存记录保存了，但数据没有的情况。
        //DataManager.getInstance().getDatabaseDataManager().updateCacheEntry(key, entry);
        //mEntrys.put(key, entry);
        synchronized (mPenddingWriteEntrys) {
            mPenddingWriteEntrys.put(key, entry);
        }
    }

//    public Entry flushCache(String key) {
//        return flushCache(key, DatabaseHelper.TYPE_NORMAL_URL);
//    }

    public Entry flushCache(String key, int type) {
        Entry entry = null;
        synchronized (mPenddingWriteEntrys) {
            entry = mPenddingWriteEntrys.remove(key);
        }
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
        //Nothing.
    }

    @Override
    public void invalidate(String key, boolean fullExpire) {
        //这个函数其实Volley没有调用。
        Entry entry = get(key);
        if (entry != null) {
            entry.softTtl = 0;
            if (fullExpire) {
                entry.ttl = 0;
            }
            put(key, entry);
//            DataManager.getInstance().getDatabaseDataManager().updateCacheTtl(key, entry.softTtl, entry.ttl);
        }
    }

    @Override
    public void remove(String key) {
        //这个函数其实Volley没有调用。
        synchronized (mEntrys) {
            mEntrys.remove(key);
        }
        synchronized (mPenddingWriteEntrys) {
            mPenddingWriteEntrys.remove(key);
        }
//        DataManager.getInstance().getDatabaseDataManager().deleteCacheEntry(key);
    }

    @Override
    public void clear() {
        synchronized (mEntrys) {
            //            mEntrys.clear();
            mEntrys.evictAll();
        }
        synchronized (mPenddingWriteEntrys) {
            mPenddingWriteEntrys.clear();
        }
//        DataManager.getInstance().getDatabaseDataManager().deleteAllCacheEntry();
    }

}
