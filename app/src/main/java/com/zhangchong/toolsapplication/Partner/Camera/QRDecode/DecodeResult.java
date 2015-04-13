package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import android.os.Bundle;

import com.google.zxing.Result;
import com.zhangchong.toolsapplication.Partner.Camera.CameraResult;

/**
 * Created by Zhangchong on 2015/4/10.
 */
public class DecodeResult implements CameraResult{
    //解析出来的结果
    Result rawResult;
    String resultText;
    //解析出来扫描的图片。
    byte[] bytes;
    float factor;

    public static Bundle getBundles(Result rawResult, byte[] imgs, float factor){
        Bundle bundle = new Bundle();
        bundle.putString(RESULT, rawResult.getText());
        bundle.putFloat(FACTOR, factor);
        bundle.putByteArray(DATA, imgs);
        return bundle;
    }

    public static DecodeResult getDecodeResult(Bundle args){
        DecodeResult result = new DecodeResult();
        result.resultText = args.getString(RESULT);
        result.factor = args.getFloat(FACTOR);
        result.bytes = args.getByteArray(DATA);
        return result;
    }


    public Result getRawResult() {
        return rawResult;
    }

    public void setRawResult(Result rawResult) {
        this.rawResult = rawResult;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }
}
