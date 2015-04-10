package com.zhangchong.toolsapplication.Partner.Camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhangchong.toolsapplication.Partner.Camera.Config.CameraConfig;
import com.zhangchong.toolsapplication.Partner.Camera.Config.CameraSurfaceCallback;
import com.zhangchong.toolsapplication.Utils.LogHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TangGe on 2015/4/6.
 */
public class CameraManager {
    public static final String TAG = CameraManager.class.getSimpleName();
    private Context mContext;

    private SurfaceView mSurfaceView;
    private CameraSurfaceCallback mSurfaceViewCallback;
    private Camera mCamera;
    private CameraConfig mCameraConfig;
    private Camera.PreviewCallback mPreviewCallback;

    private static CameraManager manager;
    public static CameraManager getCameraManager(){
        return manager;
    }

    public static synchronized CameraManager  getCameraManager(Context context){
        if(manager == null)
            manager = new CameraManager(context);
        return manager;
    }

    private CameraManager(Context context) {
        mContext = context;
        mCameraConfig = new CameraConfig(context);
    }

    public void initCameraSurface(SurfaceView surfaceView) {
        if (surfaceView == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        mSurfaceView =surfaceView;
        mSurfaceViewCallback = new CameraSurfaceCallback(this);
        surfaceView.getHolder().addCallback(mSurfaceViewCallback);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void initCameraSurface(SurfaceView surfaceView, Camera.PreviewCallback callback) {
        if (surfaceView == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        mSurfaceView =surfaceView;
        mSurfaceViewCallback = new CameraSurfaceCallback(this);
        surfaceView.getHolder().addCallback(mSurfaceViewCallback);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mPreviewCallback = callback;
    }

    public void onResume() {

    }

    public void onStart() {


    }

    public void onPause() {

    }

    public void onDestroy() {
        releaseCamera();
    }

    public CameraConfig getCameraConfig(){
        return mCameraConfig;
    }

    //切换摄像头的时候要释放之前的设备
    public void releaseCamera(){
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();//停止预览
            mCamera.release();//释放相机资源
            mCamera = null;
        }
    }

    public boolean initCameraHolder(SurfaceHolder surfaceHolder) throws IOException {
        if(!checkCameraHardware(mContext))
            return false;

        mCamera = openDefaultCamera();
        if (mCamera == null)
            return false;
        mCamera.setPreviewDisplay(surfaceHolder);
        mCameraConfig.prepareCameraParam(mCamera);
        if(mPreviewCallback != null)
            mCamera.setPreviewCallback(mPreviewCallback);
        mCamera.startPreview();
        return true;
    }

    public boolean isOpen() {
        return mCamera != null;
    }

    public Camera getCamera(){
        return mCamera;
    }

    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public List<Camera.CameraInfo> getCameraInfos() {
        int num = Camera.getNumberOfCameras();
        List<Camera.CameraInfo> cameraInfos = null;
        if (num < 1)
            return cameraInfos;

        cameraInfos = new ArrayList<>(num);
        for (int i = 0; i < num; ++i) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            cameraInfos.add(info);
        }
        return cameraInfos;
    }


    public Camera openCamera(int index) {
        Camera c = null;
        try {
            c = Camera.open(index);
        } catch (Exception e) {
            LogHelper.logE(TAG, "open camera error", e);
        }
        return c;
    }

    public Camera openDefaultCamera() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            LogHelper.logE(TAG, "open camera error", e);
        }
        return c;
    }

}
