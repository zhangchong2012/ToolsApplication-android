package com.zhangchong.toolsapplication.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.zhangchong.libnetwork.Core.Exception.AuthFailureException;
import com.zhangchong.libnetwork.Core.Exception.NetException;
import com.zhangchong.libnetwork.Core.NetworkResponse;
import com.zhangchong.libnetwork.Core.Request;
import com.zhangchong.libnetwork.Core.Response;
import com.zhangchong.libnetwork.Tools.Request.StringRequest;
import com.zhangchong.libutils.FileManager;
import com.zhangchong.libutils.LogHelper;
import com.zhangchong.toolsapplication.Data.Bean.ExcelSheetBean;
import com.zhangchong.toolsapplication.Data.ContentProvider.ToolsUri;
import com.zhangchong.toolsapplication.View.Activity.GuideActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Created by Zhangchong on 2015/4/8.
 */
public class SampleCode {
    public static void testContentProvider(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentValues> list = new ArrayList();
        ContentValues values = new ContentValues();
        ExcelSheetBean bean = new ExcelSheetBean();
//        bean.setType(ExcelSheetBean.TYPE_FILE);
//        bean.setFileName("test_file");
        ExcelSheetBean.schema.objectToValues(bean, values);
        list.add(values);

        for (int i = 0; i < 10; ++i) {
            ContentValues temp = new ContentValues();
            ExcelSheetBean bean1 = new ExcelSheetBean();
//            bean.setFileName(bean.getFileName());
            bean.setSheetIndex(i);
//            bean.setType(ExcelSheetBean.TYPE_SHEET);
//            bean.setFileName("sheet" + i);
            ExcelSheetBean.schema.objectToValues(bean, temp);
//            resolver.insert(ToolsUri.ExcelFileColumn.CONTENT_URI, values);
            list.add(temp);
        }
        ContentValues[] sss = new ContentValues[list.size()];
        sss = list.toArray(sss);
        resolver.bulkInsert(ToolsUri.ExcelFileColumn.CONTENT_URI, sss);
    }

    public static void testContentUpdateProvider(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues temp = new ContentValues();
        ExcelSheetBean bean = new ExcelSheetBean();
//        bean.setFileName(bean.getFileName());
        bean.setSheetIndex(50);
//        bean.setType(ExcelSheetBean.TYPE_SHEET);
//        bean.setFileName("sheet" + 50);
        ExcelSheetBean.schema.objectToValues(bean, temp);
        temp.remove(ToolsUri.ExcelFileColumn._ID);//Warning must delete
        resolver.update(ToolsUri.ExcelFileColumn.CONTENT_URI, temp, "file = ?", new String[]{"sheet0"});
    }

    public static void testContentQueryProvider(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ToolsUri.ExcelFileColumn.CONTENT_URI, null, null, null, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(ToolsUri.ExcelFileColumn._ID));
            String name = cursor.getString(cursor.getColumnIndex(ToolsUri.ExcelFileColumn.PARENT_FILE_ID));
//            String type=cursor.getString(cursor.getColumnIndex(ToolsUri.ExcelFileColumn.FILE_TYPE));
            String sheet = cursor.getString(cursor.getColumnIndex(ToolsUri.ExcelFileColumn.SHEET_NAME));
            String sheetindex = cursor.getString(cursor.getColumnIndex(ToolsUri.ExcelFileColumn.SHEET_INDEX));
            LogHelper.logD(GuideActivity.TAG, "id:" + id + ", name:" + name + ", sheet:" + sheet
                    + ", sheetindex:" + sheetindex);
        }
    }


    public static void testCreateXls(Context context, String fileName) {
        try {
            // 创建工作区
            File file = new File(FileManager.getInstance().getFilePath(fileName));
            if (!file.exists())
                file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
            // 创建新的一页，sheet只能在工作簿中使用
            WritableSheet sheet = workbook.createSheet("test sheet1", 0);

            // 通过函数WritableFont（）设置字体样式
            // 第一个参数表示所选字体
            // 第二个参数表示字体大小
            // 第三个参数表示粗体样式，有BOLD和NORMAL两种样式
            // 第四个参数表示是否斜体,此处true表示为斜体
            // 第五个参数表示下划线样式
            // 第六个参数表示颜色样式，此处为Red
            // Label label = new Label(col, row, title);
            //   sheet.addCell(label);
            //  mergeCells(int x,int y,int m,int n) :从第x+1列，y+1行到m+1列，n+1行合并

            WritableFont wf = new WritableFont(WritableFont.TIMES, 18,
                    WritableFont.BOLD, true, UnderlineStyle.NO_UNDERLINE,
                    Colour.RED);
            CellFormat cf = new WritableCellFormat(wf);
            // 创建单元格即具体要显示的内容，new Label(0,0,"用户") 第一个参数是column 第二个参数是row
            // 第三个参数是content，第四个参数是可选项,为Label添加字体样式
            WritableCell employee = new Label(0, 0, "雇员", cf);
            // 通过sheet的addCell方法添加Label，注意一个cell/label只能使用一次addCell
            sheet.addCell(employee);
            WritableCell sex = new Label(1, 0, "性别");
            sheet.addCell(sex);
            // 将内容写到输出流中，然后关闭工作区，最后关闭输出流
            workbook.write();
            workbook.close();
            outputStream.close();
        } catch (Exception e) {
            LogHelper.logD(GuideActivity.TAG, "exception", e);
        }
    }

    public static Request<?> testMakeRequest() {
        String url = "http://lifestyle.meizu.com/android/unauth/business/searchshop.do";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogHelper.logD(GuideActivity.TAG, "response:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(NetException error) {
                LogHelper.logD(GuideActivity.TAG, "error:" + error.getMessage());
            }
        }) {
            @Override
            public Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureException {
                Map<String, String> map = new HashMap<>();
                map.put("longitude", "113.569442");
                map.put("latitude", "22.372781");
                map.put("count", "20");
                map.put("page", "1");
                map.put("sortId", "1");
                map.put("categoryId", "35");
                map.put("cityName", "珠海");
                return map;
            }

        };
        return request;
    }
}
