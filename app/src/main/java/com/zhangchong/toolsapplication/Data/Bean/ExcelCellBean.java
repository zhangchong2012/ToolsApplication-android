package com.zhangchong.toolsapplication.Data.Bean;

import com.zhangchong.toolsapplication.Data.DAO.DaoBean;
import com.zhangchong.toolsapplication.Data.DAO.DaoBeanSchema;
import com.zhangchong.toolsapplication.Data.DAO.DaoEntry;

/**
 * Created by TangGe on 2015/4/7.
 */
public class ExcelCellBean extends DaoBean{
    public static DaoBeanSchema schema = new DaoBeanSchema(DaoBean.class);
    @Override
    protected DaoBeanSchema getSchema() {
        return schema;
    }

    public interface Columns extends DaoEntry.Columns {
        public static final String cell_row = "row";
        public static final String cell_col = "col";
        public static final String cell_content = "content";
    }
    @Column(value = Columns.cell_row)
    private int row;
    @Column(value = Columns.cell_col)
    private int col;
    @Column(value = Columns.cell_content)
    private String content;
//    private String cellLayout;
//    private String cellBorder;
//    private String cellBg;


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
