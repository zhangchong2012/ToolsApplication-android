package com.zhangchong.toolsapplication.View.Controller;

import android.app.Activity;

import com.zhangchong.libutils.FileManager;
import com.zhangchong.toolsapplication.Utils.SampleCode;

/**
 * Created by Zhangchong on 2015/4/4.
 */
public class GuideController implements IController {
    private Activity mActivity;
    private FileManager mPresenter;
    private GuideConnection mConnection;

    public GuideController(Activity activity) {
        mActivity = activity;
        mPresenter = FileManager.newInstance(activity);
        mConnection = new GuideConnection(activity);
        mConnection.bindService();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        mPresenter = null;
        mConnection.unBind();
        mConnection = null;
    }

    public String getFilePath(String fileName) {
        return mPresenter.getFilePath(fileName);
    }


    public void startRequest(){
        mConnection.getService().startRquestAsync(SampleCode.testMakeRequest());
    }
}
