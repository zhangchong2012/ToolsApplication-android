package com.zhangchong.toolsapplication.View.Controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

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
    private Camera mCamera;
    private CameraConfig mCameraConfig;
    private boolean initialized;

    public CameraManager(Context context) {
        mContext = context;
        mCameraConfig = new CameraConfig(context);
    }

    //初始化相机
    public boolean initCamera(SurfaceHolder surfaceHolder) throws IOException{
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }

        mCamera = openCamera();
        if(mCamera == null)
            return false;

        mCamera.setPreviewDisplay(surfaceHolder);
        mCamera.startPreview();
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(800, 600);
        if (!initialized) {
            initialized = true;
            mCameraConfig.initFromCamera(mCamera);
        }
        return false;
    }



    public void changeCamera(SurfaceHolder surfaceHolder, int cameraIndex){

    }

    public boolean isOpen() {
        return mCamera != null;
    }

    public void openCameraDrive(SurfaceHolder surfaceHolder) {

    }

    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public List<Camera.CameraInfo> getCameraInfos(){
        int num = Camera.getNumberOfCameras();
        List<Camera.CameraInfo> cameraInfos = null;
        if(num < 1)
            return cameraInfos;

        cameraInfos = new ArrayList<>(num);
        for (int i = 0; i < num; ++i){
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            cameraInfos.add(info);
        }
        return  cameraInfos;
    }



    public Camera openCamera(int index){
        Camera c = null;
        try {
            c = Camera.open(index);
        } catch (Exception e) {
            LogHelper.logE(TAG, "open camera error", e);
        }
        return c;
    }

    public Camera openCamera(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            LogHelper.logE(TAG, "open camera error", e);
        }
        return c;
    }


    public static class CameraConfig{
        private Context mContext;
        private Point screenResolution;
        private Point cameraResolution;

        public CameraConfig(Context context){
            this.mContext = context;
        }


        public void initFromCamera(Camera camera){
            Camera.Parameters parameters = camera.getParameters();
            WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            screenResolution = new Point(width, height);
            cameraResolution = findBestPreviewSizeValue(parameters, width, height);
        }

        public Point getScreenResolution(){
            return screenResolution;
        }

        private Point findBestPreviewSizeValue(Camera.Parameters parameters, int w, int h){
            List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
            return null;
        }

    }

}
