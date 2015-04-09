package com.zhangchong.toolsapplication.Partner.Camera.QRCode;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Zhangchong on 2015/4/9.
 */
public class DecodeThread extends  Thread{
    private DecodeHandle mDecodeHandle;

    public DecodeThread(Context context){

    }

    public Handler getHandler() {
        return mDecodeHandle;
    }


    @Override
    public void run() {
        Looper.prepare();
        mDecodeHandle = new DecodeHandle();
        Looper.loop();
    }

    public static class DecodeHandle extends Handler{
        public static final int DECODE = 0x001;
        public static final int QUIT = 0x002;
        private volatile boolean running = true;

        @Override
        public void handleMessage(Message msg) {
            if (!running) {
                return;
            }
            switch (msg.what){
                case DECODE:
                    break;
                case QUIT:
                    running = false;
                    break;
            }
        }
    }
}
