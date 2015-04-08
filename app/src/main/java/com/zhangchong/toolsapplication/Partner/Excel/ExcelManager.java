package com.zhangchong.toolsapplication.Partner.Excel;

import android.content.Context;

import com.zhangchong.toolsapplication.Data.Bean.ExcelCellBean;
import com.zhangchong.toolsapplication.Data.Bean.ExcelFileBean;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

/**
 * Created by Zhangchong on 2015/4/8.
 */
public class ExcelManager {
    private Context mContext;
    private Workbook  mBook;


    public ExcelManager(Context context){
        mContext = context;
    }


    public void exportExcelFile(File file, ExcelFileBean fileBean, ExcelCellBean cellBean) throws Exception{
        if(!file.exists())
            file.createNewFile();
        WritableWorkbook book  =  Workbook.createWorkbook(file);
    }


    public ExcelFileBean getExcelFile(){
        ExcelFileBean bean = null;
        return bean;
    }

    public ExcelCellBean getExcelCell(){
        ExcelCellBean bean = null;
        return bean;
    }
}
