package com.zhangchong.toolsapplication.View.Controller;

/**
 * Created by Zhangchong on 2015/4/4.
 */
public interface IController {
    public void onBackPressed();
    public void onResume();
    public void onStart() ;
    public void onStop() ;
    public void onDestroy();
}
