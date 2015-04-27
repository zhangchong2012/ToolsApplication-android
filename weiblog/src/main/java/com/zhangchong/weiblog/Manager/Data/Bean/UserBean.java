package com.zhangchong.weiblog.Manager.Data.Bean;

import com.zhangchong.libdao.DAO.DaoBean;
import com.zhangchong.libdao.DAO.DaoBeanSchema;
import com.zhangchong.libdao.DAO.DaoEntry;

/**
 * Created by Zhangchong on 2015/4/4.
 */
@DaoEntry.Table(value = "user")
public class UserBean extends DaoBean{
    public static DaoBeanSchema schema = new DaoBeanSchema(UserBean.class);
    @Override
    protected DaoBeanSchema getSchema() {
        return schema;
    }
    public interface Columns extends DaoEntry.Columns {
        public static final String USER_NAME = "name";
        public static final String USER_TOKEN = "token";
    }

    @Column(value = Columns.USER_NAME)
    private long time;

    @Column(value = Columns.USER_TOKEN)
    private String qrContent;


}
