package com.zhangchong.libpay;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

/**
 * @author 张充 E-mail:zchong@meizu.com
 * @version 创建时间：2014-10-1 上午11:00:54 类说明
 */
public class AliPayManager {
    private static final String TAG = AliPayManager.class.getSimpleName();
    private final Activity mContext;
    private static final int RQF_PAY_SUCCESS = 1;
    private static final int RQF_PAY_FAILED = 2;
    
    private final AliPayCallBack mCallBack;

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case RQF_PAY_SUCCESS:
                mCallBack.onResult((String) msg.obj);
                break;
            case RQF_PAY_FAILED:
                mCallBack.onError((String) msg.obj);
                break;
            default:
                break;
            }
        };
    };

    public AliPayManager(Activity context, AliPayCallBack callBack) {
        mContext = context;
        mCallBack = callBack;
    }

    public void pay(String goodsInfo) {
        final String orderInfo = goodsInfo;
        new Thread() {
            public void run() {
                try{
                    PayTask alipay = new PayTask(mContext);
                    String result = alipay.pay(orderInfo);
                    
                    Log.i(TAG, "result = " + result);
                    Message msg = mHandler.obtainMessage();
                    msg.what = RQF_PAY_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }catch(ActivityNotFoundException e){
                    Message msg = mHandler.obtainMessage();
                    msg.what = RQF_PAY_FAILED;
                    msg.obj = "请安装支付宝";
                    mHandler.sendMessage(msg);
                }catch(Exception e){
                    Message msg = mHandler.obtainMessage();
                    msg.what = RQF_PAY_FAILED;
                    msg.obj = e.getMessage();
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public static interface AliPayCallBack {
        public void onResult(String result);
        
        public void onError(String msg);
    }
}
