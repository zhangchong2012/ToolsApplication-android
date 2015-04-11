package com.zhangchong.toolsapplication.Partner.Camera.Config;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import com.zhangchong.toolsapplication.Partner.Camera.CameraManager;

import java.util.Collection;
import java.util.List;

/**
 * Created by Zhangchong on 2015/4/9.
 */
public class CameraConfig {
    private Context mContext;
    private Point cameraBestPreviewSize;
    private Point cameraBestPictureSize;

    static CameraConfig cameraConfig;
    public static CameraConfig newInstance(Context context) {
        if (cameraConfig == null)
            cameraConfig = new CameraConfig(context);
        return cameraConfig;
    }

    public static CameraConfig getInstance() {
        return cameraConfig;
    }
    private CameraConfig(Context context) {
        this.mContext = context;
    }

    public Point getCameraBestPreviewSize(){
        return cameraBestPreviewSize;
    }
    public Point getCameraBestPictureSize(){
        return cameraBestPictureSize;
    }

    private static Point findBestPreviewSizeValue(Camera.Parameters parameters, int w, int h) {
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

    private Point findBestPictureSizeValue(Camera.Parameters parameters, int w, int h) {
        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPictureSizes();
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
            Camera.Size defaultSize = parameters.getPictureSize();
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

        cameraBestPreviewSize = findBestPreviewSizeValue(parameters, width, height);
        cameraBestPictureSize = findBestPictureSizeValue(parameters, width, height);

        //orientation
        int orientation = CameraManager.getCameraManager().getCameraPreference().getCameraSettingOrientation();
        camera.setDisplayOrientation(orientation);

        //flash light
        boolean openFlash = CameraManager.getCameraManager().getCameraPreference().getCameraSettingFlash();
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
        parameters.setPreviewSize(cameraBestPreviewSize.x, cameraBestPreviewSize.y);
        parameters.setPictureSize(cameraBestPictureSize.x, cameraBestPictureSize.y);

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
