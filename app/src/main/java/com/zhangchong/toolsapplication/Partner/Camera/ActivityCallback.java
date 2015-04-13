package com.zhangchong.toolsapplication.Partner.Camera;

import android.os.Bundle;

/**
 * Created by Zhangchong on 2015/4/13.
 */
public interface ActivityCallback {
    public static final int TYPE_DECODE = 0x001;
    public void callBack(int type);
    public void callBackWithData(int type, Bundle args);
}
