package com.zhangchong.toolsapplication.Partner.Camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.text.TextUtils;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.zhangchong.toolsapplication.Utils.LogHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by TangGe on 2015/4/6.
 */
public class CameraManager {
    public static final String TAG = CameraManager.class.getSimpleName();
    private Context mContext;

    private CameraViewCallback mCameraViewCallback;
    private Camera mCamera;
    private CameraConfig mCameraConfig;
    private boolean initialized;
    private boolean mHasSurfaceView;

    public CameraManager(Context context) {
        mContext = context;
        mCameraConfig = new CameraConfig(context);
    }

    public void initCameraManager(SurfaceView surfaceView) {
        if (surfaceView == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        mCameraViewCallback = new CameraViewCallback();
        surfaceView.getHolder().addCallback(mCameraViewCallback);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void onResume() {

    }

    public void onStart() {


    }

    public void onPause() {

    }

    public void onDestroy() {
        if (mCamera != null) {
            mCamera.stopPreview();//停止预览
            mCamera.release();//释放相机资源
            mCamera = null;
        }
    }

    //初始化相机
    private boolean initCameraHolder(SurfaceHolder surfaceHolder) throws IOException {
        mCamera = openCamera();
        if (mCamera == null)
            return false;
        mCamera.setPreviewDisplay(surfaceHolder);
        initCameraParams(mCamera);
        mCamera.startPreview();
        return true;
    }


    private void initCameraParams(Camera camera) {
        if (!initialized) {
            initialized = true;
            mCameraConfig.initConfigFromCamera(mCamera);
            mCameraConfig.prepareCameraParam(mCamera);
        }
    }


    public void changeCamera(SurfaceHolder surfaceHolder, int cameraIndex) throws IOException {

    }

    public boolean isOpen() {
        return mCamera != null;
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

    public Camera openCamera() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            LogHelper.logE(TAG, "open camera error", e);
        }
        return c;
    }


    public static class CameraConfig {
        private Context mContext;
        private Point cameraBestScreen;

        public CameraConfig(Context context) {
            this.mContext = context;
        }


        public void initConfigFromCamera(Camera camera) {

        }

        private Point findBestPreviewSizeValue(Camera.Parameters parameters, int w, int h) {
            List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
            Point bestSize = null;
            float screenAspectRatio = (float) w / (float) h;

            float diff = Float.POSITIVE_INFINITY;
            for (Camera.Size supportedPreviewSize : rawSupportedSizes) {
                int realWidth = supportedPreviewSize.width;
                int realHeight = supportedPreviewSize.height;
                boolean isCandidatePortrait = realWidth < realHeight;
                int maybeFlippedWidth = isCandidatePortrait ? realWidth: realHeight;
                int maybeFlippedHeight = isCandidatePortrait ? realHeight : realWidth;
                if (maybeFlippedWidth == w && maybeFlippedHeight == h) {
                    Point exactPoint = new Point(realWidth, realHeight);
                    return exactPoint;
                }
                float aspectRatio = (float) maybeFlippedWidth / (float) maybeFlippedHeight;
                float newDiff = Math.abs(aspectRatio - screenAspectRatio);
                if (newDiff < diff) {
                    bestSize = new Point(realWidth, realHeight);
                    diff = newDiff;
                }
            }

            if (bestSize == null) {
                Camera.Size defaultSize = parameters.getPreviewSize();
                bestSize = new Point(defaultSize.width, defaultSize.height);
            }

            return bestSize;
        }


        public void prepareCameraParam(Camera camera) {
            //camera pix
            Camera.Parameters parameters = camera.getParameters();
            WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            cameraBestScreen = findBestPreviewSizeValue(parameters, width, height);

            //orientation
            int orientation = CameraPreference.getInstance(mContext).getCameraSettingOrientation();
            camera.setDisplayOrientation(orientation);

            //flash light
            boolean openFlash = CameraPreference.getInstance(mContext).getCameraSettingFlash();
            String torchMode = doSetTorch(parameters, openFlash);
            parameters.setFlashMode(torchMode);

            //auto focus
            String focus = getBestFocus(parameters);
            if (!TextUtils.isEmpty(focus)) {
                parameters.setFocusMode(focus);
            }

            String colorMode = findSettableValue(parameters.getSupportedColorEffects(),
                    Camera.Parameters.EFFECT_NEGATIVE);
            if (colorMode != null) {
                parameters.setColorEffect(colorMode);
            }
            parameters.setPreviewSize(cameraBestScreen.x, cameraBestScreen.y);
            parameters.setPictureFormat(PixelFormat.JPEG);
            camera.setParameters(parameters);
        }

        private String getBestFocus(Camera.Parameters parameters) {
            String focusMode = findSettableValue(parameters.getSupportedFocusModes(),
                    "continuous-picture", // Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE in 4.0+
                    "continuous-video",   // Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO in 4.0+
                    Camera.Parameters.FOCUS_MODE_AUTO);
            if (TextUtils.isEmpty(focusMode))
                focusMode = findSettableValue(parameters.getSupportedFocusModes(),
                        Camera.Parameters.FOCUS_MODE_AUTO);
            if (TextUtils.isEmpty(focusMode))
                findSettableValue(parameters.getSupportedFocusModes(),
                        Camera.Parameters.FOCUS_MODE_MACRO,
                        "edof"); // Camera.Parameters.FOCUS_MODE_EDOF in 2.2+
            return focusMode;
        }


        private String doSetTorch(Camera.Parameters parameters, boolean on) {
            String flashMode;
            if (on) {
                flashMode = findSettableValue(parameters.getSupportedFlashModes(),
                        Camera.Parameters.FLASH_MODE_TORCH,
                        Camera.Parameters.FLASH_MODE_ON);
            } else {
                flashMode = findSettableValue(parameters.getSupportedFlashModes(),
                        Camera.Parameters.FLASH_MODE_OFF);
            }
            return flashMode;
        }

        private String findSettableValue(Collection<String> supportedValues,
                                         String... desiredValues) {
            String result = null;
            if (supportedValues != null) {
                for (String desiredValue : desiredValues) {
                    if (supportedValues.contains(desiredValue)) {
                        result = desiredValue;
                        break;
                    }
                }
            }
            return result;
        }
    }


    public static class CameraPreference {
        public static final String PREFERENCE_CAMERA = "camera";
        public static final String PREFERENCE_FLASH = "flash";
        private SharedPreferences mPreferences;

        private static CameraPreference cameraPreference;

        public static CameraPreference getInstance(Context context) {
            if (cameraPreference == null)
                cameraPreference = new CameraPreference(context);
            return cameraPreference;
        }

        private CameraPreference(Context context) {
            mPreferences = context.getSharedPreferences(PREFERENCE_CAMERA, 0);
        }

        public boolean getCameraSettingFlash() {
            return mPreferences.getBoolean(PREFERENCE_FLASH, false);
        }

        public void setCameraSettingFlash(boolean flash) {
            mPreferences.edit().putBoolean(PREFERENCE_FLASH, false).commit();
        }

        public int getCameraSettingOrientation() {
            return mPreferences.getInt(PREFERENCE_FLASH, 90);
        }

        public void setCameraSettingOrientation(int degree) {
            mPreferences.edit().putInt(PREFERENCE_FLASH, degree).commit();
        }
    }

    class CameraViewCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!mHasSurfaceView) {
                mHasSurfaceView = true;
                try {
                    initCameraHolder(holder);
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
        }
    }
}
