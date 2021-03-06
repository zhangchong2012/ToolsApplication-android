package com.zhangchong.toolsapplication.View.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zhangchong.toolsapplication.Partner.Camera.CameraActivity;
import com.zhangchong.toolsapplication.R;
import com.zhangchong.toolsapplication.Utils.SampleCode;
import com.zhangchong.toolsapplication.View.Controller.GuideConnection;
import com.zhangchong.toolsapplication.View.Controller.GuideController;
import com.zhangchong.toolsapplication.View.Controller.IController;
import com.zhangchong.toolsapplication.View.Fragment.GuideFragment;


public class GuideActivity extends ActionBarActivity implements GuideFragment.GuideFragmentListener {
    public static final String TAG = "GUIDE";
    public static final String TAG_FRAGMENT_GUIDE = "fragment_guide";
    private IController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new GuideController(this);
        setContentView(R.layout.activity_guide);
        Fragment fragment = GuideFragment.newInstance();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.guide_container,
                    fragment, TAG_FRAGMENT_GUIDE).commit();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CameraActivity.TYPE_CAMERA_QR_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String value = data.getExtras().getString("value");
                    Toast.makeText(this, "parse ok :" + value, Toast.LENGTH_LONG);
                    ((GuideFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_GUIDE)).addText(value);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        GuideController guideController = (GuideController) mController;
        switch (position) {
            case 0:
                startActivity(CameraActivity.newIntent(this, CameraActivity.TYPE_CAMERA_QR_CODE));
                break;
            case 1:
                startActivity(CameraActivity.newIntent(this, CameraActivity.TYPE_CAMERA_PHOTO));
                break;
            case 2:
                SampleCode.testContentQueryProvider(this);
                break;
            case 3:
                SampleCode.testCreateXls(this, "test.xls");
                break;
            case 4:
                //network
                ((GuideController) mController).startRequest();
                break;
            case 5:
                //httpserver
                break;
            default:
                break;
        }
    }

}
