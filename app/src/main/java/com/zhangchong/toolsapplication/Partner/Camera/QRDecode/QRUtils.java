package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.zxing.ResultPoint;
import com.zhangchong.libutils.LogHelper;
import com.zhangchong.toolsapplication.Partner.Camera.Config.CameraPreference;

/**
 * Created by Zhangchong on 2015/4/13.
 */
public class QRUtils {
    public static Rect calculateQrRect(Rect previewRect, Rect scanRect, Rect cameraRect){
        Rect rect = null;
//        Rect scanRect =((QrDecodeFragment)(((DecodeManager)callback).getFragment())).getMaskView().getScanFrame();
//        Rect previewRect =((QrDecodeFragment)(((DecodeManager)callback).getFragment())).getMaskView().getPreviewFrame();
        int degree = CameraPreference.getInstance().getCameraSettingOrientation();
        if (previewRect == null || scanRect == null || cameraRect == null) {
            return rect;
        }
        rect = new Rect();

        if(degree % 90 ==0){
            rect.left = scanRect.left * cameraRect.height() / previewRect.width();
            rect.right = scanRect.right * cameraRect.height() / previewRect.width();
            rect.top = scanRect.top * cameraRect.width() / previewRect.height();
            rect.bottom = scanRect.bottom * cameraRect.width() / previewRect.height();

            //以 0,0为原点，旋转90度
            Point leftTop = QRUtils.rotate90Degree(Math.toRadians(90), rect.left, rect.top);
            Point rightBottom = QRUtils.rotate90Degree(Math.toRadians(90), rect.right, rect.bottom);
            rect.set(Math.abs(leftTop.x), Math.abs(leftTop.y), Math.abs(rightBottom.x), Math.abs(rightBottom.y));
        }else{
            rect.left = scanRect.left * cameraRect.width() / previewRect.width();
            rect.right = scanRect.right * cameraRect.width() / previewRect.width();
            rect.top = scanRect.top * cameraRect.height() / previewRect.height();
            rect.bottom = scanRect.bottom * cameraRect.height() / previewRect.height();
        }

        return rect;
    }

    /**
     * c=r*cos(δ+β)=r*cos(δ)cos(β)-r*sin(δ)sin(β)=xcos(β)-ysin(β)

     　d=r*sin(δ+β)=r*sin(δ)cos(β)+r*cos(δ)sin(β)=ycos(β)+xsin(β)
     x1=x0cosn-y0sinn
     y1=x0sinn+y0cosn
     */
    public static Point rotate90Degree(double degree, float x, float y){
        Point point = new Point();
        point.x = (int)(x * Math.cos(degree) - y *Math.sin(degree));
        point.y = (int)(y * Math.cos(degree) + x *Math.sin(degree));
        return point;
    }


    public static ResultPoint calculatePossiblePoint(ResultPoint point, int preWidth, int preHeight){
        LogHelper.logD("point", "start point.x is：" + point.getX() + ", point.y is:" + point.getY());
        int degree = CameraPreference.getInstance().getCameraSettingOrientation();
        if(degree%90 == 0){
            //计算的点都是旋转后的屏幕。要平移一个屏幕到当前屏幕范围内
            Point temp = rotate90Degree(Math.toRadians(degree), point.getX(), point.getY());
            LogHelper.logD("point", "rotate temp.x is：" +point.getX() + ", temp.y is:" + point.getY());
            if(temp.x < 0){
                temp.x= preWidth/2 + temp.x;
            }
            if(temp.y < 0){
                temp.y= preHeight/2 + temp.y;
            }
            point = new ResultPoint(Math.abs(temp.x), Math.abs(temp.y));

            LogHelper.logD("point", "final point.x is：" +point.getX() + ", point.y is:" + point.getY());
            temp = null;
        }
        return point;
    }

    public  static void   StartFragment(FragmentManager manager, Fragment fragment, Bundle args){

    }

}
