package com.zhangchong.toolsapplication.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhangchong.toolsapplication.R;
import com.zhangchong.toolsapplication.View.Controller.CameraManager;

public class CameraActivity extends ActionBarActivity {
    public static final String TAG = CameraActivity.class.getSimpleName();

    private SurfaceView mCameraView;
    private CameraViewCallback mCameraViewCallback;
    private CameraManager mCameraManager;

    private boolean mHasSurfaceView;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, CameraActivity.class);
        return intent;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraManager = new CameraManager(this);
        setContentView(R.layout.activity_camera);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews(){
        mCameraView = (SurfaceView)findViewById(R.id.camera_view);
        mCameraViewCallback = new CameraViewCallback();
        mCameraView.getHolder().addCallback(mCameraViewCallback);
        mCameraView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class CameraViewCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if(!mHasSurfaceView){
                mHasSurfaceView = true;
                try{
                    mCameraManager.initCamera(holder);
                }catch (Exception e){
                    mHasSurfaceView = false;
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mHasSurfaceView = false;
        }
    }
}
