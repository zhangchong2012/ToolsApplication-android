package com.zhangchong.weiblog.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.zhangchong.weiblog.Fragment.LoginFragment;
import com.zhangchong.weiblog.Manager.WeiblogManager;
import com.zhangchong.weiblog.R;


public class MainActivity extends ActionBarActivity implements  LoginFragment.OnFragmentLoginListener{
    private WeiblogManager mWeiblogManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeiblogManager = WeiblogManager.newInstance(this);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().add(R.id.main_container, LoginFragment.newInstance(), LoginFragment.TAG).commit();
        }
    }

    @Override
    protected void onDestroy() {
        if(mWeiblogManager != null){
            mWeiblogManager.onDestroy();
            mWeiblogManager = null;
        }
        super.onDestroy();
    }

    @Override
    public void onLogin(String name, String pwd) {
        mWeiblogManager.requestLogin(name, pwd);
    }

    @Override
    public void onRegister() {
//        mWeiblogManager.requestRegister();
        //show register fragment
    }
}
