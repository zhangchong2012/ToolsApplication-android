package com.zhangchong.toolsapplication.Partner.Camera.TakePhoto;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Zhangchong on 2015/4/10.
 */
public class PictureSaveTread extends  Thread{
    private SaveHandler mHandler;
    public static class SaveHandler extends  Handler{
        private boolean isRunning;
        public static final int SAVE = 0x001;
        public static final int QUIT = 0x002;
        @Override
        public void handleMessage(Message msg) {
            if(!isRunning)
                return;
            switch (msg.what){
                case SAVE:
                    if(msg.obj != null){
                        savePicture((byte [])msg.obj);
                    }
                    break;
                case QUIT:
                    isRunning =false;
                    break;
            }
        }
        private void savePicture(byte[] data){

        }
    }

    public SaveHandler getHandler(){
        return mHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        mHandler = new SaveHandler();
        Looper.loop();
    }
}
