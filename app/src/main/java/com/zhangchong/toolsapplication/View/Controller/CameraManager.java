package com.zhangchong.toolsapplication.View.Controller;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;

/**
 * Created by TangGe on 2015/4/6.
 */
public class CameraManager {
    public static final String TAG = CameraManager.class.getSimpleName();
    private Context mContext;
    private Camera mCamera;

    public CameraManager(Context context){
        mContext = context;
    }

    public boolean initCamera(SurfaceHolder surfaceHolder){
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if(isOpen()){
            return  true;
        }
        return false;
    }

    public boolean isOpen(){
        return mCamera != null;
    }

    public void openCameraDrive(SurfaceHolder surfaceHolder){

    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

}
