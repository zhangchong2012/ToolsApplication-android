package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import android.graphics.Rect;

/**
 * Created by Zhangchong on 2015/4/13.
 */
public class QRUtils {
    public static Rect calculateQrRect(Rect previewRect, Rect scanRect, Rect cameraRect){
        Rect rect = null;
//        Rect scanRect =((QrDecodeFragment)(((DecodeManager)callback).getFragment())).getMaskView().getScanFrame();
//        Rect previewRect =((QrDecodeFragment)(((DecodeManager)callback).getFragment())).getMaskView().getPreviewFrame();
        if (previewRect == null || scanRect == null || cameraRect == null) {
            return rect;
        }
        rect = new Rect();
        rect.left = scanRect.left * cameraRect.width() / previewRect.width();
        rect.right = scanRect.right * cameraRect.width() / previewRect.width();
        rect.top = scanRect.top * cameraRect.height() / previewRect.height();
        rect.bottom = scanRect.bottom * cameraRect.height() / previewRect.height();
        return rect;
    }
}
