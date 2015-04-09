package com.zhangchong.toolsapplication.Partner.Camera.Config;

import android.view.SurfaceHolder;

import com.zhangchong.toolsapplication.Partner.Camera.CameraManager;

/**
 * Created by Zhangchong on 2015/4/9.
 */
public class CameraSurfaceCallback implements SurfaceHolder.Callback {
    private boolean mHasSurfaceView;
    private CameraManager mCameraManager;
    public CameraSurfaceCallback(CameraManager cameraManager){
        mCameraManager = cameraManager;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mHasSurfaceView) {
            mHasSurfaceView = true;
            try {
                mCameraManager.initCameraHolder(holder);
            } catch (Exception e) {
                mHasSurfaceView = false;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurfaceView = false;
        mCameraManager.releaseCamera();
    }
}
