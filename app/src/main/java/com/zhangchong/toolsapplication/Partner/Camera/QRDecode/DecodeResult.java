package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import com.google.zxing.Result;

/**
 * Created by Zhangchong on 2015/4/10.
 */
public class DecodeResult {
    //解析出来的结果
    Result rawResult;

    //解析出来扫描的图片。
    byte[] bytes;
    float factor;
}
