package com.zhangchong.toolsapplication.Data.Bean;

import com.zhangchong.toolsapplication.Data.DAO.DaoBean;
import com.zhangchong.toolsapplication.Data.DAO.DaoBeanSchema;
import com.zhangchong.toolsapplication.Data.DAO.DaoEntry;

/**
 * Created by Zhangchong on 2015/4/4.
 */
@DaoEntry.Table(value = "qr")
public class QRBean extends DaoBean{
    public static DaoBeanSchema schema = new DaoBeanSchema(QRBean.class);
    @Override
    protected DaoBeanSchema getSchema() {
        return schema;
    }
    public interface Columns extends DaoEntry.Columns {
        public static final String QR_TIME = "time";
        public static final String QR_CONTENT = "content";
    }

    @Column(value = Columns.QR_TIME)
    private long time;

    @Column(value = Columns.QR_CONTENT)
    private String qrContent;


}
