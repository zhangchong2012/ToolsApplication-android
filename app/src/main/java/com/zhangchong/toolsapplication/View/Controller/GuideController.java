package com.zhangchong.toolsapplication.View.Controller;

import android.app.Activity;

import com.zhangchong.toolsapplication.Presenter.ApplicationPresenter;

/**
 * Created by Zhangchong on 2015/4/4.
 */
public class GuideController implements IController {
    private Activity mActivity;
    private ApplicationPresenter mPresenter;

    public GuideController(Activity activity) {
        mActivity = activity;
        mPresenter = new ApplicationPresenter(activity.getApplicationContext());
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
    }

    public String getFilePath(String fileName) {
        return mPresenter.getFilePath(fileName);
    }

}
