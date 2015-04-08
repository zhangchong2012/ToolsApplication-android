package com.zhangchong.toolsapplication.View.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zhangchong.toolsapplication.R;
import com.zhangchong.toolsapplication.Utils.LogHelper;
import com.zhangchong.toolsapplication.Utils.SampleCode;
import com.zhangchong.toolsapplication.View.Controller.GuideController;
import com.zhangchong.toolsapplication.View.Controller.IController;
import com.zhangchong.toolsapplication.View.Fragment.GuideFragment;


public class GuideActivity extends ActionBarActivity implements GuideFragment.GuideFragmentListener{
    public static final String TAG = "GUIDE";
    public static final String TAG_FRAGMENT_GUIDE = "fragment_guide";

    private IController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new GuideController(this);
        setContentView(R.layout.activity_guide);
        Fragment fragment = GuideFragment.newInstance();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.guide_container,
                    fragment , TAG_FRAGMENT_GUIDE).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mController.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mController.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mController.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mController.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.onDestroy();
        mController = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(int position) {
        GuideController guideController = (GuideController)mController;
        switch (position){
            case 0:
                SampleCode.testCreateXls(this, "test.xls");
                break;
            case 1:
                SampleCode.testContentProvider(this);
                break;
            case 2:
                SampleCode.testContentUpdateProvider(this);
                break;
            case 3:
                SampleCode.testContentQueryProvider(this);
                break;
            default:
                break;
        }
    }

}
