package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

/**
 * Created by Zhangchong on 2015/4/9.
 * 解码管理器。因为扫描解码都是通过相机，所以这一模块的内容都在相机下面
 */
public class DecodeManager implements IDecodeCallback {
    private DecodeThread mDecodeTread;
    private Handler mStatusHandler;
    private Fragment mFragment;
    public DecodeManager(Fragment fragment, Handler statusHandler){
        mFragment = fragment;
        mDecodeTread = new DecodeThread(this);
        mDecodeTread.start();
        mStatusHandler = statusHandler;
    }

    public Fragment getFragment(){
        return mFragment;
    }

    @Override
    public void callDecode(byte[] data, int w, int h) {
        if(mDecodeTread.getHandler() == null)
            return;
        Message msg = mDecodeTread.getHandler().obtainMessage(DecodeThread.DecodeHandle.DECODE, w, h, data);
        msg.sendToTarget();
    }

    @Override
    public void CallDecodeStatus(int status, DecodeResult result) {
        if(mStatusHandler == null)
            return;
        int msgWhat = 0;
        switch (status){
            case IDecodeCallback.STATUS_OK:
                msgWhat = QrDecodeFragment.QrHandler.DECODE_SUCCESS;
            case IDecodeCallback.STATUS_ERROR:
                msgWhat = QrDecodeFragment.QrHandler.DECODE_FAILED;
        }
        Message msg = mStatusHandler.obtainMessage(msgWhat, result);
//        msg.obj = result.rawResult.getText();
        msg.sendToTarget();
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
        ResultPoint temp = new ResultPoint(point.getY(), point.getX());
        Message msg = mStatusHandler.obtainMessage(QrDecodeFragment.QrHandler.DECODE_POSSIBLE, point);
        msg.sendToTarget();
    }

    public void onDestroy(){
        mDecodeTread.onDestroy();
    }

}
