package com.zhangchong.toolsapplication.Partner.Excel;

import com.zhangchong.toolsapplication.Data.Bean.ExcelCellBean;
import com.zhangchong.toolsapplication.Data.Bean.ExcelSheetBean;

import java.util.List;

/**
 * Created by TangGe on 2015/4/8.
 */
public class ExcelBean {
    private String fileName;
    private List<SheetBean> sheets;
    private List<CellBean> cells;

    public static  class SheetBean{
        private int index;
        private String name;
    }
    public static  class CellBean{
        private int row;
        private int col;
        private String content;
    }

    public static ExcelBean convertToExcelBean(List<ExcelSheetBean> fileBeans, List<ExcelCellBean> cellBeans){
        ExcelBean excel = null;
        for (int i = 0; i < fileBeans.size(); i++) {
            ExcelSheetBean bean = fileBeans.get(i);

        }
        return excel;
    }
}
