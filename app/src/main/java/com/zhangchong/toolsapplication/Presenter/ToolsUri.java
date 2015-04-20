package com.zhangchong.toolsapplication.Presenter;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

import com.zhangchong.libnetwork.Tools.Cache.CacheBean;
import com.zhangchong.libnetwork.Tools.Cache.DataBaseCache;
import com.zhangchong.libutils.Constant;
import com.zhangchong.toolsapplication.Data.Bean.ExcelCellBean;
import com.zhangchong.toolsapplication.Data.Bean.ExcelSheetBean;

/**
 * Created by TangGe on 2015/4/8.
 */
public class ToolsUri implements Constant{

    public static final String EXCEL_FILE = "excel_file";
    public static final String EXCEL_FILE_ITEM = "excel_file/#";
    public static final String EXCEL_CELL = "excel_cell";
    public static final String EXCEL_CELL_ITEM = "excel_cell/#";

    static interface ExcelUri {
        public static final int EXCEL_FILE = 0x0010;
        public static final int EXCEL_FILE_ID = 0x0011;
        public static final int EXCEL_CELL = 0x0015;
        public static final int EXCEL_CELL_ID = 0x0016;


    }
    public static final int NETWORK_CACHE = 0x0021;
    public static final int NETWORK_CACHE_ID = 0x0022;

    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(Constant.LEGACY_AUTHORITY, EXCEL_FILE, ExcelUri.EXCEL_FILE);
        URI_MATCHER.addURI(Constant.LEGACY_AUTHORITY, EXCEL_FILE_ITEM, ExcelUri.EXCEL_FILE_ID);
        URI_MATCHER.addURI(Constant.LEGACY_AUTHORITY, EXCEL_CELL, ExcelUri.EXCEL_CELL);
        URI_MATCHER.addURI(Constant.LEGACY_AUTHORITY, EXCEL_CELL_ITEM, ExcelUri.EXCEL_CELL_ID);
        URI_MATCHER.addURI(Constant.LEGACY_AUTHORITY , CacheBean.TAG, NETWORK_CACHE);
        URI_MATCHER.addURI(Constant.LEGACY_AUTHORITY , CacheBean.TAG + "/#", NETWORK_CACHE_ID);
    }

    public  static  class ExcelFileColumn implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.parse("content://" + Constant.LEGACY_AUTHORITY + "/" + EXCEL_FILE);
        // 表数据列
        public static final String PARENT_FILE_ID = ExcelSheetBean.Columns.excel_file_id;
        public static final String  SHEET_NAME = ExcelSheetBean.Columns.excel_name_sheet;
        public static final String  SHEET_INDEX = ExcelSheetBean.Columns.excel_name_sheet_index;

        public static ExcelSheetBean parseContentValues(ContentValues values){
            if(values == null)
                return null;
            ExcelSheetBean bean = new ExcelSheetBean();
            return  ExcelSheetBean.schema.valuesToObject(values, bean);
        }

        public static ContentValues parseExcelFileBean(ExcelSheetBean bean){
            if(bean == null)
                return null;
            ContentValues values = new ContentValues();
            ExcelSheetBean.schema.objectToValues(bean, values);
            return values;
        }
    }

    public  static  class ExcelCellColumn implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.parse("content://" + Constant.LEGACY_AUTHORITY + EXCEL_CELL);
        public static final String  CELL_ROW = ExcelCellBean.Columns.cell_row;
        public static final String  CELL_COL = ExcelCellBean.Columns.cell_col;
        public static final String  CELL_CONTENT = ExcelCellBean.Columns.cell_content;
    }
}
