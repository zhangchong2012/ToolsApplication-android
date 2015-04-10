package com.zhangchong.toolsapplication.Data.Bean;

import com.zhangchong.toolsapplication.Data.DAO.DaoBean;
import com.zhangchong.toolsapplication.Data.DAO.DaoBeanSchema;
import com.zhangchong.toolsapplication.Data.DAO.DaoEntry;

/**
 * Created by Zhangchong on 2015/4/10.
 */
@DaoEntry.Table(value = "files")
public class FileBean extends DaoBean {
    public static DaoBeanSchema schema = new DaoBeanSchema(ExcelCellBean.class);
    @Override
    protected DaoBeanSchema getSchema() {
        return schema;
    }

    public static interface Columns extends DaoEntry.Columns {
        public static final String file_name = "file_name";
        public static final String file_path = "file_path";
    }

    @Column(value = Columns.file_name)
    private String fileName;
    @Column(value = Columns.file_path)
    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
