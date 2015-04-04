package com.zhangchong.toolsapplication.Data.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zhangchong.toolsapplication.Utils.LogHelper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DaoEntrySchema {
    //解析entry的规则。重点解析，对应的数据库的表名，Columns的字段名和数据的type。
    //解析的过程是利用java的annotation的注释解析的。
    private static final String TAG = "LifeEntrySchema";

    static final int TYPE_STRING = 0;
    static final int TYPE_BOOLEAN = 1;
    static final int TYPE_SHORT = 2;
    static final int TYPE_INT = 3;
    static final int TYPE_LONG = 4;
    static final int TYPE_FLOAT = 5;
    static final int TYPE_DOUBLE = 6;
    static final int TYPE_BLOB = 7;
    static final int TYPE_LIST = 8;
    static final int TYPE_ENUM = 9;

    private static final String SQLITE_TYPES[] = { "TEXT", "INTEGER", "INTEGER", "INTEGER", "INTEGER", "REAL", "REAL",
            "NONE", "TEXT", "TEXT" };

    private static final String FULL_TEXT_INDEX_SUFFIX = "_fulltext";

    private final String mTableName;
    final ColumnInfo[] mColumnInfo;
    private final String[] mProjection;
    private final String[] mColumnNames;
    private final boolean mHasFullTextIndex;

    DaoEntrySchema(Class<? extends DaoEntry> clazz) {
        // Get table and column metadata from reflection.
        ColumnInfo[] columns = parseColumnInfo(clazz);
        mTableName = parseTableName(clazz);
        mColumnInfo = columns;

        // Cache the list of projection columns and check for full-text columns.
        String[] projection = {};
        boolean hasFullTextIndex = false;
        int columnNameCount = 0;
        if (columns != null) {
            projection = new String[columns.length];
            for (int i = 0; i != columns.length; ++i) {
                ColumnInfo column = columns[i];
                projection[i] = column.name;
                if (column.fullText) {
                    hasFullTextIndex = true;
                }
                if (column.visible) {
                    ++columnNameCount;
                }
            }
        }
        mProjection = projection;
        mHasFullTextIndex = hasFullTextIndex;

        mColumnNames = new String[columnNameCount];
        int i = 0;
        if (columns != null) {
            for (ColumnInfo column : columns) {
                if (column.visible) {
                    mColumnNames[i++] = column.name;
                } else {
                    break;
                }
            }
        }
    }

    public String getTableName() {
        return mTableName;
    }

    public ColumnInfo[] getColumnInfo() {
        return mColumnInfo;
    }

    public String[] getProjection() {
        return mProjection;
    }

    public String[] getColumnNames() {
        return mColumnNames;
    }

    int getColumnIndex(String columnName) {
        for (ColumnInfo column : mColumnInfo) {
            if (column.name.equals(columnName)) {
                return column.projectionIndex;
            }
        }
        return -1;
    }

    ColumnInfo getColumn(String columnName) {
        int index = getColumnIndex(columnName);
        return (index < 0) ? null : mColumnInfo[index];
    }

    public String getColumnType(String columnName) {
        ColumnInfo info = getColumn(columnName);
        if (info == null || info.type > SQLITE_TYPES.length - 1) {
            return SQLITE_TYPES[TYPE_STRING];
        }
        return SQLITE_TYPES[info.type];
    }

    private void logExecSql(SQLiteDatabase db, String sql) {
        db.execSQL(sql);
    }

    <T extends DaoEntry> T cursorToObject(Cursor cursor, T object) {
        try {
            for (ColumnInfo column : mColumnInfo) {
                int columnIndex = column.projectionIndex;
                Field field = column.field;
                switch (column.type) {
                case TYPE_STRING:
                    field.set(object, cursor.isNull(columnIndex) ? null : cursor.getString(columnIndex));
                    break;
                case TYPE_BOOLEAN:
                    field.setBoolean(object, cursor.getShort(columnIndex) == 1);
                    break;
                case TYPE_SHORT:
                    field.setShort(object, cursor.getShort(columnIndex));
                    break;
                case TYPE_INT:
                    field.setInt(object, cursor.getInt(columnIndex));
                    break;
                case TYPE_LONG:
                    field.setLong(object, cursor.getLong(columnIndex));
                    break;
                case TYPE_FLOAT:
                    field.setFloat(object, cursor.getFloat(columnIndex));
                    break;
                case TYPE_DOUBLE:
                    field.setDouble(object, cursor.getDouble(columnIndex));
                    break;
                case TYPE_BLOB:
                    field.set(object, cursor.isNull(columnIndex) ? null : cursor.getBlob(columnIndex));
                    break;
                case TYPE_LIST:
                    //add by 郭城
                    //增加List类型数据解析
                    valuesToListObject(field, object, cursor.getString(columnIndex));
                    break;
                case TYPE_ENUM:
                    //TODO
                    //add by 郭城
                    //增加Enum类型数据解析
                    valuesToEnumObject(field, object, cursor.getString(columnIndex));
                    break;
                }
            }
            return object;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    <T extends DaoEntry> T cursorToObject(Cursor cursor, Class<T> clazz, boolean close) {
        if (cursor == null) {
            return null;
        }
        try {
            T entry = null;
            if (cursor.moveToFirst()) {
                entry = clazz.newInstance();
                cursorToObject(cursor, entry);
            }
            return entry;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (close) {
                cursor.close();
            }
        }
    }

    <T extends DaoEntry> T cursorToObject(Cursor cursor, DaoEntry.Creator<T> creator, boolean close) {
        if (cursor == null) {
            return null;
        }
        try {
            T entry = null;
            if (cursor.moveToFirst()) {
                entry = creator.create();
                cursorToObject(cursor, entry);
            }
            return entry;
        } finally {
            if (close) {
                cursor.close();
            }
        }

    }

    <T extends DaoEntry> List<T> cursorToObjectList(Cursor cursor, Class<T> clazz, boolean close) {
        if (cursor == null) {
            return null;
        }
        ArrayList<T> list = new ArrayList<T>(cursor.getCount());
        try {
            if (cursor.moveToFirst()) {
                do {
                    T entry = clazz.newInstance();
                    cursorToObject(cursor, entry);
                    list.add(entry);
                } while (cursor.moveToNext());
            }
            return list;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (close) {
                cursor.close();
            }
        }
    }

    <T extends DaoEntry> List<T> cursorToObjectList(Cursor cursor, DaoEntry.Creator<T> creator, boolean close) {
        if (cursor == null) {
            return null;
        }
        ArrayList<T> list = new ArrayList<T>(cursor.getCount());
        try {
            if (cursor.moveToFirst()) {
                do {
                    T entry = creator.create();
                    cursorToObject(cursor, entry);
                    list.add(entry);
                } while (cursor.moveToNext());
            }
            return list;
        } finally {
            if (close) {
                cursor.close();
            }
        }
    }

    private void setIfNotNull(Field field, Object object, Object value) throws IllegalAccessException {
        if (value != null)
            field.set(object, value);
    }

    /**
     * Converts the ContentValues to the object. The ContentValues may not
     * contain values for all the fields in the object.
     */
    public <T extends DaoEntry> T valuesToObject(ContentValues values, T object) {
        try {
            for (ColumnInfo column : mColumnInfo) {
                String columnName = column.name;
                Field field = column.field;
                switch (column.type) {
                case TYPE_STRING:
                    setIfNotNull(field, object, values.getAsString(columnName));
                    break;
                case TYPE_BOOLEAN:
                    setIfNotNull(field, object, values.getAsBoolean(columnName));
                    break;
                case TYPE_SHORT:
                    setIfNotNull(field, object, values.getAsShort(columnName));
                    break;
                case TYPE_INT:
                    setIfNotNull(field, object, values.getAsInteger(columnName));
                    break;
                case TYPE_LONG:
                    setIfNotNull(field, object, values.getAsLong(columnName));
                    break;
                case TYPE_FLOAT:
                    setIfNotNull(field, object, values.getAsFloat(columnName));
                    break;
                case TYPE_DOUBLE:
                    setIfNotNull(field, object, values.getAsDouble(columnName));
                    break;
                case TYPE_BLOB:
                    setIfNotNull(field, object, values.getAsByteArray(columnName));
                    break;
                case TYPE_LIST:
                    //add by 郭城
                    //增加List类型数据解析
                    valuesToListObject(field, object, values.getAsString(columnName));
                    break;
                case TYPE_ENUM:
                    //add by 郭城
                    //增加List类型数据解析
                    valuesToEnumObject(field, object, values.getAsString(columnName));
                    break;
                }
            }
            return object;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    void valuesToListObject(Field field, Object object, String dbString) throws IllegalAccessException {
        if (TextUtils.isEmpty(dbString)) {
            return;
        }
        Type listType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        if (listType == String.class) {
            setIfNotNull(field, object, stringToStringList(dbString));
        } else if (listType == Integer.class) {
            setIfNotNull(field, object, stringToIntegerList(dbString));
        } else if (listType == Double.class) {
            setIfNotNull(field, object, stringToDoubleList(dbString));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void valuesToEnumObject(Field field, Object object, String dbString) throws IllegalAccessException {
        if (TextUtils.isEmpty(dbString)) {
            return;
        }
        Class<Enum> enumType = (Class<Enum>) field.getType();
        field.set(object, Enum.valueOf(enumType, dbString));
    }

    void objectToValues(DaoEntry object, ContentValues values) {
        objectToValues(object, values, true);
    }

    void objectToValues(DaoEntry object, ContentValues values, boolean containsFieldDefaultValue) {
        try {
            DaoEntry copy = null;
            if (!containsFieldDefaultValue) {
                copy = object.getClass().newInstance();
            }
            for (ColumnInfo column : mColumnInfo) {
                String columnName = column.name;
                Field field = column.field;
                switch (column.type) {
                case TYPE_STRING:
                    if (containsFieldDefaultValue || field.get(object) != field.get(copy)) {
                        values.put(columnName, (String) field.get(object));
                    }
                    break;
                case TYPE_BOOLEAN:
                    if (containsFieldDefaultValue || field.getBoolean(object) != field.getBoolean(copy)) {
                        values.put(columnName, field.getBoolean(object));
                    }
                    break;
                case TYPE_SHORT:
                    if (containsFieldDefaultValue || field.getShort(object) != field.getShort(copy)) {
                        values.put(columnName, field.getShort(object));
                    }
                    break;
                case TYPE_INT:
                    if (containsFieldDefaultValue || field.getInt(object) != field.getInt(copy)) {
                        values.put(columnName, field.getInt(object));
                    }
                    break;
                case TYPE_LONG:
                    if (containsFieldDefaultValue || field.getLong(object) != field.getLong(copy)) {
                        values.put(columnName, field.getLong(object));
                    }
                    break;
                case TYPE_FLOAT:
                    if (containsFieldDefaultValue || field.getFloat(object) != field.getFloat(copy)) {
                        values.put(columnName, field.getFloat(object));
                    }
                    break;
                case TYPE_DOUBLE:
                    if (containsFieldDefaultValue || field.getDouble(object) != field.getDouble(copy)) {
                        values.put(columnName, field.getDouble(object));
                    }
                    break;
                case TYPE_BLOB:
                    if (containsFieldDefaultValue || field.get(object) != field.get(copy)) {
                        values.put(columnName, (byte[]) field.get(object));
                    }
                    break;
                case TYPE_LIST:
                    //add by 郭城
                    //增加List类型数据封装
                    if (containsFieldDefaultValue || field.get(object) != field.get(copy)) {
                        List<?> list = (List<?>) field.get(object);
                        String listStr = listToString(list);
                        values.put(columnName, listStr);
                    }
                    break;
                case TYPE_ENUM:
                    //TODO 
                    //add by 郭城
                    //增加Enum类型数据封装
                    if (containsFieldDefaultValue || field.get(object) != field.get(copy)) {
                        Object enumObj = field.get(object);
                        if (enumObj != null) {
                            values.put(columnName, enumObj.toString());
                        }
                    }
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public String toDebugString(DaoEntry entry) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("ID=").append(entry.mId);
            for (ColumnInfo column : mColumnInfo) {
                String columnName = column.name;
                Field field = column.field;
                Object value = field.get(entry);
                sb.append(" ").append(columnName).append("=").append((value == null) ? "null" : value.toString());
            }
            return sb.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String toDebugString(DaoEntry entry, String... columnNames) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("ID=").append(entry.mId);
            for (String columnName : columnNames) {
                ColumnInfo column = getColumn(columnName);
                Field field = column.field;
                Object value = field.get(entry);
                sb.append(" ").append(columnName).append("=").append((value == null) ? "null" : value.toString());
            }
            return sb.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    Cursor query(SQLiteDatabase db, String selection, String[] selectionArgs, String groupBy, String having,
                 String orderBy) {
        return db.query(mTableName, mProjection, selection, selectionArgs, groupBy, having, orderBy);
    }

    Cursor query(SQLiteDatabase db, String selection, String[] selectionArgs, String groupBy, String having,
                 String orderBy, String limit) {
        return db.query(mTableName, mProjection, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 
     * 简单封装的SELECT SUM语句
     * 
     * @param db
     *            数据库句柄
     * @param sumColumn
     *            选择SUM的数据库Column
     * @param selection
     * @param selectionArgs
     * @return SUM的个数
     */
    public int rawSumQuery(SQLiteDatabase db, String sumColumn, String selection, String[] selectionArgs) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT SUM(").append(sumColumn).append(") FROM ").append(getTableName());

        if (!TextUtils.isEmpty(selection)) {
            strBuilder.append(" WHERE ").append(selection);
        }

        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.rawQuery(strBuilder.toString(), selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            LogHelper.logE(TAG, "[rawSumQuery] from DB Exception.");
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * 
     * 简单封装的SELECT COUNT语句
     * 
     * @param db
     *            数据库句柄
     * @param countColumn
     *            选择COUNT的数据库Column
     * @param selection
     * @param selectionArgs
     * @return COUNT的个数
     */
    public int rawCountQuery(SQLiteDatabase db, String countColumn, String selection, String[] selectionArgs) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SELECT COUNT(").append(countColumn).append(") FROM ").append(getTableName());

        if (!TextUtils.isEmpty(selection)) {
            strBuilder.append(" WHERE ").append(selection);
        }
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.rawQuery(strBuilder.toString(), selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            LogHelper.logE(TAG, "[rawCountQuery] from DB Exception.");
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    /**
     * 简单封装的SQL语句
     * 
     * @param db
     *            数据库句柄
     * @param clazz
     *            反射结果类
     * @param sql
     *            SQL语句
     * @param selectionArgs
     * @return
     */
    public <T extends DaoEntry> List<T> rawQuery(SQLiteDatabase db, Class<T> clazz, String sql, String[] selectionArgs) {
        List<T> list = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, selectionArgs);
            list = cursorToObjectList(cursor, clazz, false);
        } catch (Exception e) {
            LogHelper.logE(TAG, "[rawQuery] from DB Exception.");
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    Cursor queryAll(SQLiteDatabase db) {
        return db.query(mTableName, mProjection, null, null, null, null, null);
    }

    public <T extends DaoEntry> T queryObject(SQLiteDatabase db, Class<T> clazz, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = query(db, selection, selectionArgs, groupBy, having, orderBy);
        return cursorToObject(cursor, clazz, true);
    }

    public <T extends DaoEntry> T queryObject(SQLiteDatabase db, DaoEntry.Creator<T> creator, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = query(db, selection, selectionArgs, groupBy, having, orderBy);
        return cursorToObject(cursor, creator, true);
    }

    public <T extends DaoEntry> List<T> queryObjects(SQLiteDatabase db, Class<T> clazz, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = query(db, selection, selectionArgs, groupBy, having, orderBy);
        return cursorToObjectList(cursor, clazz, true);
    }

    public <T extends DaoEntry> List<T> queryObjects(SQLiteDatabase db, Class<T> clazz, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = query(db, selection, selectionArgs, groupBy, having, orderBy, limit);
        return cursorToObjectList(cursor, clazz, true);
    }

    public <T extends DaoEntry> List<T> queryObjects(SQLiteDatabase db, DaoEntry.Creator<T> creator, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = query(db, selection, selectionArgs, groupBy, having, orderBy);
        return cursorToObjectList(cursor, creator, true);
    }

    public <T extends DaoEntry> List<T> queryAllObjects(SQLiteDatabase db, Class<T> clazz) {
        return cursorToObjectList(queryAll(db), clazz, true);
    }

    public <T extends DaoEntry> List<T> queryAllObjects(SQLiteDatabase db, DaoEntry.Creator<T> creator) {
        return cursorToObjectList(queryAll(db), creator, true);
    }

    public boolean queryWithId(SQLiteDatabase db, long id, DaoEntry entry) {
        Cursor cursor = db
                .query(mTableName, mProjection, "_id=?", new String[] { Long.toString(id) }, null, null, null);
        boolean success = false;
        if (cursor.moveToFirst()) {
            cursorToObject(cursor, entry);
            success = true;
        }
        cursor.close();
        return success;
    }

    public long insertOrReplace(SQLiteDatabase db, DaoEntry entry) {
        ContentValues values = new ContentValues();
        objectToValues(entry, values);
        if (entry.mId == 0) {
            values.remove("_id");
        }
        long id = db.replace(mTableName, "_id", values);
        entry.mId = id;
        return id;
    }

    public <T extends DaoEntry> void insertOrReplace(SQLiteDatabase db, Collection<T> entries) {
        if (entries == null)
            return;

        ContentValues values;
        for (DaoEntry entry : entries) {
            values = new ContentValues();
            objectToValues(entry, values);
            if (entry.mId == 0) {
                values.remove("_id");
            }
            long id = db.replace(mTableName, "_id", values);
            entry.mId = id;
        }
        // LogHelper.logW("ReaderEntrySchema", "insertOrReplace end" + entries);
    }

    public <T extends DaoEntry> void insertOrReplace(SQLiteDatabase db, Collection<T> entries, boolean useTransaction) {

        if (useTransaction) {
            db.beginTransaction();
            try {
                insertOrReplace(db, entries);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } else {
            insertOrReplace(db, entries);
        }
    }

    // public void update(SQLiteDatabase db, ReaderEntry entry) {
    // update(db, entry, null, null, false);
    // }
    //
    // public void update(SQLiteDatabase db, ReaderEntry entry, boolean
    // containsFieldDefaultValue) {
    // update(db, entry, null, null, containsFieldDefaultValue);
    // }
    //
    // public void update(SQLiteDatabase db, ReaderEntry entry, String
    // selection, String[] selectionArgs) {
    // update(db, entry, selection, selectionArgs, false);
    // }
    //
    // public void update(SQLiteDatabase db, ReaderEntry entry, String
    // selection, String[] selectionArgs,
    // boolean containsFieldDefaultValue) {
    // ContentValues values = new ContentValues();
    // objectToValues(entry, values, containsFieldDefaultValue);
    // update(db, entry, values, selection, selectionArgs);
    // }

    public void update(SQLiteDatabase db, ContentValues values, String selection, String[] selectionArgs) {
        db.update(mTableName, values, selection, selectionArgs);
    }

    public void update(SQLiteDatabase db, DaoEntry entry, ContentValues values, String selection,
            String[] selectionArgs) {
        values.remove("_id");

        if (values.size() == 0)
            return;

        StringBuilder whereBuilder = new StringBuilder();
        ArrayList<String> whereArgsList = new ArrayList<String>();

        if (entry != null && entry.mId != 0) {
            whereBuilder.append("_id = ?");
            whereArgsList.add(Long.toString(entry.mId));
        }

        if (selection != null && selectionArgs != null) {
            if (!TextUtils.isEmpty(whereBuilder.toString())) {
                whereBuilder.append(" AND ");
            }
            whereBuilder.append(selection);
            whereArgsList.addAll(Arrays.asList(selectionArgs));
        }
        String whereClause = whereBuilder.toString();
        String[] whereArgs = whereArgsList.toArray(new String[whereArgsList.size()]);

        db.update(mTableName, values, whereClause, whereArgs);
    }

    public boolean deleteWithId(SQLiteDatabase db, long id) {
        return db.delete(mTableName, "_id=?", new String[] { Long.toString(id) }) == 1;
    }

    public boolean delete(SQLiteDatabase db, String selection, String[] selectionArgs) {
        return db.delete(mTableName, selection, selectionArgs) > 0;
    }

    public void createTables(SQLiteDatabase db) {
        // Wrapped class must have a @Table.Definition.
        String tableName = mTableName;
//        Utils.assertTrue(tableName != null);

        // Add the CREATE TABLE statement for the main table.
        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(tableName);
        sql.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT");
        StringBuilder unique = new StringBuilder();
        boolean replaceOnConflict = false;
        for (ColumnInfo column : mColumnInfo) {
            if (!column.isId()) {
                sql.append(',');
                sql.append(column.name);
                sql.append(' ');
                sql.append(SQLITE_TYPES[column.type]);
                if (!TextUtils.isEmpty(column.defaultValue)) {
                    sql.append(" DEFAULT ");
                    sql.append(column.defaultValue);
                }
                if (column.unique) {
                    if (unique.length() == 0) {
                        unique.append(column.name);
                    } else {
                        unique.append(',').append(column.name);
                    }
                    replaceOnConflict = column.replaceOnConflict;
                }
            }
        }
        if (unique.length() > 0) {
            sql.append(",UNIQUE(").append(unique).append(')');
            if (replaceOnConflict) {
                sql.append(" ON CONFLICT REPLACE");
            }
        }
        sql.append(");");
        logExecSql(db, sql.toString());
        sql.setLength(0);

        // Create indexes for all indexed columns.
        for (ColumnInfo column : mColumnInfo) {
            // Create an index on the indexed columns.
            if (column.indexed) {
                sql.append("CREATE INDEX ");
                sql.append(tableName);
                sql.append("_index_");
                sql.append(column.name);
                sql.append(" ON ");
                sql.append(tableName);
                sql.append(" (");
                sql.append(column.name);
                sql.append(");");
                logExecSql(db, sql.toString());
                sql.setLength(0);
            }
        }

        if (mHasFullTextIndex) {
            // Add an FTS virtual table if using full-text search.
            String ftsTableName = tableName + FULL_TEXT_INDEX_SUFFIX;
            sql.append("CREATE VIRTUAL TABLE ");
            sql.append(ftsTableName);
            sql.append(" USING FTS3 (_id INTEGER PRIMARY KEY");
            for (ColumnInfo column : mColumnInfo) {
                if (column.fullText) {
                    // Add the column to the FTS table.
                    String columnName = column.name;
                    sql.append(',');
                    sql.append(columnName);
                    sql.append(" TEXT");
                }
            }
            sql.append(");");
            logExecSql(db, sql.toString());
            sql.setLength(0);

            // Build an insert statement that will automatically keep the FTS
            // table in sync.
            StringBuilder insertSql = new StringBuilder("INSERT OR REPLACE INTO ");
            insertSql.append(ftsTableName);
            insertSql.append(" (_id");
            for (ColumnInfo column : mColumnInfo) {
                if (column.fullText) {
                    insertSql.append(',');
                    insertSql.append(column.name);
                }
            }
            insertSql.append(") VALUES (new._id");
            for (ColumnInfo column : mColumnInfo) {
                if (column.fullText) {
                    insertSql.append(",new.");
                    insertSql.append(column.name);
                }
            }
            insertSql.append(");");
            String insertSqlString = insertSql.toString();

            // Add an insert trigger.
            sql.append("CREATE TRIGGER ");
            sql.append(tableName);
            sql.append("_insert_trigger AFTER INSERT ON ");
            sql.append(tableName);
            sql.append(" FOR EACH ROW BEGIN ");
            sql.append(insertSqlString);
            sql.append("END;");
            logExecSql(db, sql.toString());
            sql.setLength(0);

            // Add an update trigger.
            sql.append("CREATE TRIGGER ");
            sql.append(tableName);
            sql.append("_update_trigger AFTER UPDATE ON ");
            sql.append(tableName);
            sql.append(" FOR EACH ROW BEGIN ");
            sql.append(insertSqlString);
            sql.append("END;");
            logExecSql(db, sql.toString());
            sql.setLength(0);

            // Add a delete trigger.
            sql.append("CREATE TRIGGER ");
            sql.append(tableName);
            sql.append("_delete_trigger AFTER DELETE ON ");
            sql.append(tableName);
            sql.append(" FOR EACH ROW BEGIN DELETE FROM ");
            sql.append(ftsTableName);
            sql.append(" WHERE _id = old._id; END;");
            logExecSql(db, sql.toString());
            sql.setLength(0);
        }
    }

    /**
     * 增加数据库某字段
     * @param db 
     * @param columnName 字段名
     */
    public void addColumn(SQLiteDatabase db, String columnName) {
        //1.先去查找是否包含这个字段对应的ColumnInfo
        ColumnInfo info = getColumn(columnName);
        if (info == null) {
            // 1.a 如果没有，则啥事不做
            LogHelper.logD(TAG, mTableName + "addColumn " + columnName + " Failed");
            return;
        }
        //2.封装添加字段的SQL语句
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ");
        sql.append(mTableName);
        sql.append(" ADD COLUMN ");
        sql.append(columnName);
        sql.append(" ");
        sql.append(SQLITE_TYPES[info.type]);
        LogHelper.logD(TAG, "addColumn SQL " + sql.toString());
        //3.执行SQL语句
        logExecSql(db, sql.toString());
    }

    public void dropTables(SQLiteDatabase db) {
        String tableName = mTableName;
        StringBuilder sql = new StringBuilder("DROP TABLE IF EXISTS ");
        sql.append(tableName);
        sql.append(';');
        logExecSql(db, sql.toString());
        sql.setLength(0);

        if (mHasFullTextIndex) {
            sql.append("DROP TABLE IF EXISTS ");
            sql.append(tableName);
            sql.append(FULL_TEXT_INDEX_SUFFIX);
            sql.append(';');
            logExecSql(db, sql.toString());
        }
    }

    public void deleteAll(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(mTableName);
        sql.append(";");
        logExecSql(db, sql.toString());
    }

    String parseTableName(Class<? extends Object> clazz) {
        // Check for a table annotation.
        DaoEntry.Table table = clazz.getAnnotation(DaoEntry.Table.class);
        if (table == null) {
            return null;
        }

        // Return the table name.
        return table.value();
    }

    ColumnInfo[] parseColumnInfo(Class<? extends Object> clazz) {
        ArrayList<ColumnInfo> columns = new ArrayList<ColumnInfo>();
        while (clazz != null) {
            parseColumnInfo(clazz, columns);
            clazz = clazz.getSuperclass();
        }
        Collections.sort(columns);
        int i = 0;
        for (ColumnInfo column : columns) {
            column.projectionIndex = i++;
        }
        return columns.toArray(new ColumnInfo[columns.size()]);
    }

    void parseColumnInfo(Class<? extends Object> clazz, ArrayList<ColumnInfo> columns) {
        // Gather metadata from each annotated field.
        Field[] fields = clazz.getDeclaredFields(); // including non-public
                                                    // fields
        for (int i = 0; i != fields.length; ++i) {
            // Get column metadata from the annotation.
            Field field = fields[i];
            DaoEntry.Column info = field.getAnnotation(DaoEntry.Column.class);
            if (info == null)
                continue;

            // Determine the field type.
            int type;
            Class<?> fieldType = field.getType();
            if (fieldType == String.class) {
                type = TYPE_STRING;
            } else if (fieldType == boolean.class) {
                type = TYPE_BOOLEAN;
            } else if (fieldType == short.class) {
                type = TYPE_SHORT;
            } else if (fieldType == int.class) {
                type = TYPE_INT;
            } else if (fieldType == long.class) {
                type = TYPE_LONG;
            } else if (fieldType == float.class) {
                type = TYPE_FLOAT;
            } else if (fieldType == double.class) {
                type = TYPE_DOUBLE;
            } else if (fieldType == byte[].class) {
                type = TYPE_BLOB;
            } else if (fieldType.isEnum()) {
                type = TYPE_ENUM;
            } else if (fieldType == List.class) {
                type = TYPE_LIST;
            } else {
                throw new IllegalArgumentException("Unsupported field type for column: " + fieldType.getName());
            }

            // Add the column to the array.
            int index = columns.size();
            columns.add(new ColumnInfo(info.value(), type, info.indexed(), info.unique(), info.fullText(), info
                    .defaultValue(), info.visible(), field, index, info.replaceOnConflict()));
        }
    }

    public static class ColumnInfo implements Comparable<ColumnInfo> {
        private static final String ID_KEY = "_id";

        public final String name;
        public int type;
        public final boolean indexed;
        public final boolean unique;
        public final boolean fullText;
        public final String defaultValue;
        public final Field field;
        public final boolean visible;
        public int projectionIndex;
        public final boolean replaceOnConflict;

        public ColumnInfo(String name, int type, boolean indexed, boolean unique, boolean fullText,
                String defaultValue, boolean visible, Field field, int projectionIndex, boolean replaceOnConflict) {
            this.name = name.toLowerCase(Locale.CHINESE);
            this.type = type;
            this.indexed = indexed;
            this.unique = unique;
            this.fullText = fullText;
            this.defaultValue = defaultValue;
            this.field = field;
            this.projectionIndex = projectionIndex;
            this.visible = visible;
            this.replaceOnConflict = replaceOnConflict;
            field.setAccessible(true); // in order to set non-public fields
        }

        public boolean isId() {
            return ID_KEY.equals(name);
        }

        @Override
        public int compareTo(ColumnInfo another) {
            if (visible != another.visible) {
                return visible ? -1 : 1;
            } else {
                return projectionIndex - another.projectionIndex;
            }
        }
    }

    private static String listToString(List<?> list) {
        String result = "";
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (Object object : list) {
                builder.append(String.valueOf(object)).append(",");
            }
            result = builder.toString();
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private static List<String> stringToStringList(String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        String[] items = s.split(",");
        ArrayList<String> result = new ArrayList<String>();
        for (String i : items) {
            result.add(i);
        }
        return result;
    }

    private static List<Integer> stringToIntegerList(String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        String[] items = s.split(",");
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (String i : items) {
            result.add(Integer.valueOf(i));
        }
        return result;
    }

    private static List<Double> stringToDoubleList(String s) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        String[] items = s.split(",");
        ArrayList<Double> result = new ArrayList<Double>();
        for (String i : items) {
            result.add(Double.valueOf(i));
        }
        return result;
    }
}
