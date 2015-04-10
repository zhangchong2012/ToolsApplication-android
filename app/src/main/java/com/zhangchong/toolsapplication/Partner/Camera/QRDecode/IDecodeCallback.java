package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import com.google.zxing.ResultPointCallback;

/**
 * Created by Zhangchong on 2015/4/10.
 */
public interface IDecodeCallback extends ResultPointCallback {
    public static final int STATUS_OK = 1;
    public static final int STATUS_ERROR = 2;
    public void callDecode(byte[] data, int w, int h);
    public void CallDecodeStatus(int status, DecodeResult result);
}
