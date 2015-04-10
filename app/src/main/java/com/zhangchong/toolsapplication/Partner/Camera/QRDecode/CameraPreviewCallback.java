package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import android.graphics.Point;
import android.hardware.Camera;

import com.zhangchong.toolsapplication.Partner.Camera.CameraManager;

/**
 * Created by Zhangchong on 2015/4/9.
 */
public class CameraPreviewCallback implements Camera.PreviewCallback{
    private IDecodeCallback mDecodeCallback;

    public CameraPreviewCallback(IDecodeCallback decodeCallback){
        mDecodeCallback = decodeCallback;
    }
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        CameraManager cameraManager = CameraManager.getCameraManager();
        if (cameraManager == null)
            return;
        Point point = cameraManager.getCameraConfig().getCameraBestPictureSize();
        if(point == null)
            return;
        mDecodeCallback.callDecode(data, point.x, point.y);
    }
}
