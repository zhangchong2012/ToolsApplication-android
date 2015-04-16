package com.zhangchong.libdao.DAO;

public abstract class DaoBean extends DaoEntry {

    protected abstract DaoBeanSchema getSchema();

    public String getString(int index) {
        return getSchema().getString(this, index);
    }

    public boolean getBoolean(int index) {
        return getSchema().getBoolean(this, index);
    }

    public short getShort(int index) {
        return getSchema().getShort(this, index);
    }

    public int getInt(int index) {
        return getSchema().getInt(this, index);
    }

    public long getLong(int index) {
        return getSchema().getLong(this, index);
    }

    public float getFloat(int index) {
        return getSchema().getFloat(this, index);
    }

    public double getDouble(int index) {
        return getSchema().getDouble(this, index);
    }

    public byte[] getBlob(int index) {
        return getSchema().getBlob(this, index);
    }

    public boolean isNull(int index) {
        return getSchema().isNull(this, index);
    }

}
