package com.zhangchong.toolsapplication.Partner.Camera;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zhangchong.toolsapplication.Partner.Camera.QRDecode.DecodeResult;
import com.zhangchong.toolsapplication.Partner.Camera.QRDecode.QrDecodeFragment;
import com.zhangchong.toolsapplication.Partner.Camera.QRDecode.QrResultFragment;
import com.zhangchong.toolsapplication.Partner.Camera.TakePhoto.TakePhotoFragment;
import com.zhangchong.toolsapplication.R;

public class CameraActivity extends FragmentActivity
        implements ActivityCallback {
    public static final String TAG = CameraActivity.class.getSimpleName();
    public static final String TYPE_CAMERA = "camera_type";
    public static final int TYPE_CAMERA_PHOTO = 0x001;
    public static final int TYPE_CAMERA_QR_CODE = 0x002;

    private CameraManager mCameraManager;

    private int mType;


    public static Intent newIntent(Context context, int cameraType) {
        Intent intent = new Intent(context, CameraActivity.class);
        Bundle args = new Bundle();
        args.putInt(TYPE_CAMERA, cameraType);
        intent.putExtras(args);
        return intent;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraManager = CameraManager.getCameraManager(this);
        boolean support = mCameraManager.checkCameraHardware(this);
        if (!support) {
            finish();
        }
        setContentView(R.layout.activity_camera);

        Bundle args = getIntent().getExtras();
        mType = args.getInt(TYPE_CAMERA);
        switch (mType) {
            case TYPE_CAMERA_PHOTO:
                getSupportFragmentManager().beginTransaction().add(R.id.camera_container,
                        TakePhotoFragment.newInstance(), TakePhotoFragment.TAG).commit();
                break;
            case TYPE_CAMERA_QR_CODE:
                getSupportFragmentManager().beginTransaction().add(R.id.camera_container,
                        QrDecodeFragment.newInstance(), QrDecodeFragment.TAG).commit();
                break;
        }
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
        mCameraManager.onDestroy();
        super.onDestroy();
    }

    public CameraManager getCameraManager() {
        return mCameraManager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
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
    public void callBack(int type) {

    }

    @Override
    public void callBackWithData(int type, Bundle args) {
        switch (type) {
            case TYPE_DECODE: {
                if (args == null)
                    return;
                DecodeResult result = DecodeResult.getDecodeResult(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.camera_container,
                        QrResultFragment.newInstance(QrResultFragment.PAGE_SCAN_RESULT,
                                result.getResultText(), result.getBytes(), result.getFactor()
                        ), QrResultFragment.TAG).commit();
            }
            break;
        }
    }

}
