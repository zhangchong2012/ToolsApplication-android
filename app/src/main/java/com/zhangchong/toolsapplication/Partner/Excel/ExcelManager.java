package com.zhangchong.toolsapplication.Partner.Excel;

import android.content.Context;

import com.zhangchong.toolsapplication.Data.Bean.ExcelCellBean;
import com.zhangchong.toolsapplication.Data.Bean.ExcelSheetBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jxl.Sheet;
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
import jxl.write.WriteException;

/**
 * Created by Zhangchong on 2015/4/8.
 */
public class ExcelManager {
    private Context mContext;
    private Workbook  mBook;


    public ExcelManager(Context context){
        mContext = context;
    }


    public void exportExcelFile(File file, List<ExcelSheetBean> fileBeans) throws Exception{

//        FileOutputStream outputStream = new FileOutputStream(file);
//        WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
        WritableWorkbook book  =  createWorkBook(file);

        for (int i = 0; i < fileBeans.size(); i++) {
            ExcelSheetBean sheetBean = fileBeans.get(i);
            WritableSheet sheet = createWorkSheet(book, sheetBean.getSheetName(), sheetBean.getSheetIndex());
            List<ExcelCellBean> cellBeans = sheetBean.getCellBeans();
            if(cellBeans == null || cellBeans.size() == 0)
                continue;
            for (int j = 0; j < cellBeans.size(); j++) {
                ExcelCellBean cellBean = cellBeans.get(j);
                addCellToSheet(sheet, cellBean.getCol(), cellBean.getRow(), cellBean.getContent(), null);
            }
        }


        book.write();
        book.close();

//        outputStream.close();
    }

    private WritableWorkbook createWorkBook(String fileName) throws IOException{
        File file = new File(fileName);
        return  createWorkBook(file);
    }

    private WritableWorkbook createWorkBook(File file) throws IOException{
        if(!file.exists())
            file.createNewFile();
        return  Workbook.createWorkbook(file);
    }

    public WritableSheet createWorkSheet(WritableWorkbook book, String sheetName, int sheetIndex){
        return book.createSheet(sheetName, sheetIndex);
    }

    public void addCellToSheet (WritableSheet sheet, int c, int r, String name, CellFormat format) throws WriteException{
        WritableCell cell = new Label(c, r, name, format);
        sheet.addCell(cell);

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
//        WritableFont wf = new WritableFont(WritableFont.TIMES, 18,
//                WritableFont.BOLD, true, UnderlineStyle.NO_UNDERLINE,
//                Colour.RED);
//        CellFormat cf = new WritableCellFormat(wf);
//        WritableCell employee = new Label(0, 0, "雇员", cf);
    }



}
