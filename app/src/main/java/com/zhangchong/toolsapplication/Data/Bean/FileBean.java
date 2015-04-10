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
}
