package com.zhangchong.libnetwork.Tools.Cache;

import android.net.Uri;

import com.zhangchong.libdao.DAO.DaoBean;
import com.zhangchong.libdao.DAO.DaoBeanSchema;
import com.zhangchong.libdao.DAO.DaoEntry;
import com.zhangchong.libutils.Constant;

/**
 * Created by Zhangchong on 2015/4/17.
 */
@DaoEntry.Table(value = "cache_entry")
public class CacheBean extends DaoBean {
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constant.LEGACY_AUTHORITY + "/" + "network_cache");

    public static DaoBeanSchema schema = new DaoBeanSchema(CacheBean.class);
    @Override
    protected DaoBeanSchema getSchema() {
        return schema;
    }

    public interface Columns extends DaoEntry.Columns {
        public static final String COLUMN_ENTRY_CACHE_URL = "url";
        public static final String COLUMN_ENTRY_CACHE_ETAG = "etag";
        public static final String COLUMN_ENTRY_CACHE_SERVER_DATE = "serverDate";
        public static final String COLUMN_ENTRY_CACHE_TTL = "ttl";
        public static final String COLUMN_ENTRY_CACHE_SOFT_TTL = "softTtl";
        public static final String COLUMN_ENTRY_CACHE_RESPONSE_HEADER = "responseHeaders";
        public static final String COLUMN_ENTRY_CACHE_HEADER_LENGTH_INFO = "header_length_info";
        public static final String COLUMN_ENTRY_CACHE_BYTE_SIZE = "byte_size";
        public static final String COLUMN_ENTRY_CACHE_TYPE = "type";
    }

    @Column(value = Columns.COLUMN_ENTRY_CACHE_URL, unique = true, replaceOnConflict = true)
    private String url;
    @Column(value = Columns.COLUMN_ENTRY_CACHE_ETAG)
    private String etag;
    @Column(value = Columns.COLUMN_ENTRY_CACHE_SERVER_DATE)
    private long serverDate;
    @Column(value = Columns.COLUMN_ENTRY_CACHE_TTL)
    private long ttl;
    @Column(value = Columns.COLUMN_ENTRY_CACHE_SOFT_TTL)
    private long softTtl;
    @Column(value = Columns.COLUMN_ENTRY_CACHE_RESPONSE_HEADER)
    private String responseHeaders;
    @Column(value = Columns.COLUMN_ENTRY_CACHE_HEADER_LENGTH_INFO)
    private String headerLengthInfo;
    @Column(value = Columns.COLUMN_ENTRY_CACHE_BYTE_SIZE)
    private long byteSize;
    @Column(value = Columns.COLUMN_ENTRY_CACHE_TYPE)
    private int type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public long getServerDate() {
        return serverDate;
    }

    public void setServerDate(long serverDate) {
        this.serverDate = serverDate;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public long getSoftTtl() {
        return softTtl;
    }

    public void setSoftTtl(long softTtl) {
        this.softTtl = softTtl;
    }

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getHeaderLengthInfo() {
        return headerLengthInfo;
    }

    public void setHeaderLengthInfo(String headerLengthInfo) {
        this.headerLengthInfo = headerLengthInfo;
    }

    public long getByteSize() {
        return byteSize;
    }

    public void setByteSize(long byteSize) {
        this.byteSize = byteSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
