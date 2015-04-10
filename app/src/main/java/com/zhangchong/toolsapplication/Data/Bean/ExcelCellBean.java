package com.zhangchong.toolsapplication.Data.Bean;

import com.zhangchong.toolsapplication.Data.DAO.DaoBean;
import com.zhangchong.toolsapplication.Data.DAO.DaoBeanSchema;
import com.zhangchong.toolsapplication.Data.DAO.DaoEntry;

/**
 * Created by TangGe on 2015/4/7.
 */
@DaoEntry.Table(value = "excel_cell")
public class ExcelCellBean extends DaoBean{
    public static DaoBeanSchema schema = new DaoBeanSchema(ExcelCellBean.class);
    @Override
    protected DaoBeanSchema getSchema() {
        return schema;
    }

    public static interface Columns extends DaoEntry.Columns {
        public static final String excel_file_id = "parent_id";
        public static final String cell_row = "row";
        public static final String cell_col = "col";
        public static final String cell_content = "content";
    }
    @Column(value = Columns.excel_file_id)
    private int sheetId;
    @Column(value = Columns.cell_row)
    private int row;
    @Column(value = Columns.cell_col)
    private int col;
    @Column(value = Columns.cell_content)
    private String content;
//    private String cellLayout;
//    private String cellBorder;
//    private String cellBg;


    public int getSheetId() {
        return sheetId;
    }

    public void setSheetId(int sheetId) {
        this.sheetId = sheetId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
