package com.zhangchong.toolsapplication.Partner.Camera.Config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zhangchong on 2015/4/9.
 */
public class CameraPreference {
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