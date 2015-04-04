package com.zhangchong.toolsapplication.Data.DAO;

import java.lang.reflect.Field;

public class DaoBeanSchema extends DaoEntrySchema {

    public DaoBeanSchema(Class<? extends DaoBean> clazz) {
        super(clazz);
    }

    public String getString(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            Object obj = field.get(bean);
            if (obj == null) {
                return null;
            } else {
                return obj.toString();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean getBoolean(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            return field.getBoolean(bean);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public short getShort(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            return field.getShort(bean);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getInt(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            return field.getInt(bean);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getLong(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            return field.getLong(bean);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float getFloat(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            return field.getFloat(bean);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getDouble(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            return field.getDouble(bean);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public byte[] getBlob(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            return (byte[]) field.get(bean);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isNull(DaoBean bean, int index) {
        Field field = mColumnInfo[index].field;
        try {
            Object obj = field.get(bean);
            return obj == null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return true;
        }
        return true;
    }
}
