package com.zhangchong.toolsapplication.Partner.Camera.TakePhoto;

import android.hardware.Camera;
import android.os.Message;

/**
 * Created by Zhangchong on 2015/4/10.
 */
public class CameraPhotoCallBack implements Camera.ShutterCallback, Camera.PictureCallback{
    private PictureSaveTread.SaveHandler mHandler;
    @Override
    public void onShutter() {

    }

    public void setSaveHandler(PictureSaveTread.SaveHandler handler){
        mHandler = handler;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //save picture
        Message msg = mHandler.obtainMessage(PictureSaveTread.SaveHandler.SAVE, data);
        msg.sendToTarget();
        camera.startPreview();
    }
}
