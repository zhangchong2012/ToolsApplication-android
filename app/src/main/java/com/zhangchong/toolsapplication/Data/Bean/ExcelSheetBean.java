package com.zhangchong.toolsapplication.Data.Bean;

import com.zhangchong.toolsapplication.Data.DAO.DaoBean;
import com.zhangchong.toolsapplication.Data.DAO.DaoBeanSchema;
import com.zhangchong.toolsapplication.Data.DAO.DaoEntry;

import java.util.List;

/**
 * Created by TangGe on 2015/4/7.
 */
@DaoEntry.Table(value = "excel_sheet")
public class ExcelSheetBean extends DaoBean {
    public static DaoBeanSchema schema = new DaoBeanSchema(ExcelSheetBean.class);

    @Override
    protected DaoBeanSchema getSchema() {
        return schema;
    }

    public static interface Columns extends DaoEntry.Columns {
        public static final String excel_file_id = "parent_id";
        public static final String excel_name_sheet = "sheet_name";
        public static final String excel_name_sheet_index = "sheet_index";
    }

    @Column(value = Columns.excel_file_id)
    private int fileId;
    @Column(value = Columns.excel_name_sheet)
    private String sheetName;
    @Column(value = Columns.excel_name_sheet_index)
    private int sheetIndex;


    private List<ExcelCellBean> cellBeans;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public List<ExcelCellBean> getCellBeans() {
        return cellBeans;
    }

    public void setCellBeans(List<ExcelCellBean> cellBeans) {
        this.cellBeans = cellBeans;
    }
}
