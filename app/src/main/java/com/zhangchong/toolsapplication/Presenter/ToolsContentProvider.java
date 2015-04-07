package com.zhangchong.toolsapplication.Presenter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.zhangchong.toolsapplication.Data.SqlManager;

public class ToolsContentProvider extends ContentProvider {
    private SqlManager mSqlManager;

    private static final int ALLPERSON=1;
    private static final int PERSON=2;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{

        uriMatcher.addURI("com.tools.provider", "file", ALLPERSON);
        uriMatcher.addURI("com.tools.provider", "person/#", PERSON);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        mSqlManager = new SqlManager(getContext());
        return mSqlManager != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
