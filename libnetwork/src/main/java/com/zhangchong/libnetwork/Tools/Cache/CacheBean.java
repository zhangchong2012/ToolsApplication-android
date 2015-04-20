package com.zhangchong.libnetwork.Tools.Cache;

import android.content.ContentValues;
import android.net.Uri;

import com.zhangchong.libdao.DAO.DaoBean;
import com.zhangchong.libdao.DAO.DaoBeanSchema;
import com.zhangchong.libdao.DAO.DaoEntry;
import com.zhangchong.libnetwork.Core.Cache;
import com.zhangchong.libutils.Constant;

import java.util.Map;

/**
 * Created by Zhangchong on 2015/4/17.
 */
@DaoEntry.Table(value = "cache_entry")
public class CacheBean extends DaoBean {
    public static final String TAG = "network_cache";
    public static final Uri CONTENT_URI = Uri.parse("content://" + Constant.LEGACY_AUTHORITY + "/" + TAG);

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

    public static interface TYPE{
        public static final int TYPE_NORMAL = 0;
        public static final int TYPE_FILE = 1;
        public static final int TYPE_IMG = 2;
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


    public static ContentValues convertToContentValues(String key, Cache.Entry cacheEntry){
        if(cacheEntry == null)
            return null;
        ContentValues values = new ContentValues();
        values.put(Columns.COLUMN_ENTRY_CACHE_ETAG, cacheEntry.etag);
        values.put(Columns.COLUMN_ENTRY_CACHE_SERVER_DATE, cacheEntry.serverDate);
        values.put(Columns.COLUMN_ENTRY_CACHE_TTL, cacheEntry.ttl);
        values.put(Columns.COLUMN_ENTRY_CACHE_SOFT_TTL, cacheEntry.softTtl);

        StringBuilder sbHeader = new StringBuilder(300);
        StringBuilder sbHeaderInfo = new StringBuilder(100);
        for (Map.Entry<String, String> e : cacheEntry.responseHeaders.entrySet()) {
            sbHeader.append(e.getKey());
            sbHeader.append("=");
            sbHeader.append(e.getValue());
            sbHeader.append(",");
            sbHeaderInfo.append(e.getKey().length());
            sbHeaderInfo.append(",");
            sbHeaderInfo.append(e.getValue().length());
            sbHeaderInfo.append(",");
        }
        String header = sbHeader.toString();
        String headerInfo = sbHeaderInfo.toString();
        values.put(Columns.COLUMN_ENTRY_CACHE_RESPONSE_HEADER, header);
        values.put(Columns.COLUMN_ENTRY_CACHE_HEADER_LENGTH_INFO, headerInfo);

        values.put(Columns.COLUMN_ENTRY_CACHE_URL, key);
        int byteSize = 0;

        //TODO 如果是图片或者文件，应该放入到文件系统里面。暂时先放入到数据库中
        if (cacheEntry.data != null) {
            byteSize = cacheEntry.data.length;
        }
        values.put(Columns.COLUMN_ENTRY_CACHE_BYTE_SIZE, byteSize);
        values.put(Columns.COLUMN_ENTRY_CACHE_TYPE, TYPE.TYPE_NORMAL);
        return values;
    }
}
