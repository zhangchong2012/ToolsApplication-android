package com.zhangchong.toolsapplication.Data.Bean;

import com.zhangchong.toolsapplication.Data.DAO.DaoBean;
import com.zhangchong.toolsapplication.Data.DAO.DaoBeanSchema;
import com.zhangchong.toolsapplication.Data.DAO.DaoEntry;

/**
 * Created by TangGe on 2015/4/7.
 */
@DaoEntry.Table(value = "excel_file")
public class ExcelFileBean extends DaoBean {
    public static DaoBeanSchema schema = new DaoBeanSchema(DaoBean.class);
    public static final int TYPE_FILE = 0;
    public static final int TYPE_SHEET = 1;
    @Override
    protected DaoBeanSchema getSchema() {
        return schema;
    }

    public interface Columns extends DaoEntry.Columns {
        public static final String excel_name_file = "file";
        public static final String excel_name_sheet = "sheet";
        public static final String excel_name_sheet_index = "index";
        public static final String excel_name_type = "type";
    }

    @Column(value = Columns.excel_name_file)
    private String fileName;
    @Column(value = Columns.excel_name_sheet)
    private String sheetName;
    @Column(value = Columns.excel_name_sheet_index)
    private String sheetIndex;
    @Column(value = Columns.excel_name_type)
    private int type;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(String sheetIndex) {
        this.sheetIndex = sheetIndex;
    }
}
