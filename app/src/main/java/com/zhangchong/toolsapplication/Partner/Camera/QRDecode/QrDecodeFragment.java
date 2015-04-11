package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.zhangchong.toolsapplication.Partner.Camera.CameraActivity;
import com.zhangchong.toolsapplication.Partner.Camera.QRDecode.ZxingCode.ViewfinderView;
import com.zhangchong.toolsapplication.R;

import java.lang.ref.WeakReference;

public class QrDecodeFragment extends Fragment {
    public static final String TAG = "QrCodeFragment";
    private SurfaceView mCameraView;
    private DecodeManager mDecodeManager;
    private Handler mHandler;

    private ViewfinderView mMaskView;

    public static QrDecodeFragment newInstance() {
        QrDecodeFragment fragment = new QrDecodeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new QrHandler(this);
        mDecodeManager = new DecodeManager(this, mHandler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_decode, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View rootView){
        UIClick click = new UIClick();
        mCameraView = (SurfaceView)rootView.findViewById(R.id.decode_preview);
        ((CameraActivity)getActivity()).getCameraManager().initCameraSurface(mCameraView,
                new CameraPreviewCallback(mDecodeManager));
        mMaskView = (ViewfinderView)rootView.findViewById(R.id.decode_mask);
    }

    public  ViewfinderView getMaskView(){
        return mMaskView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnQRDecodeListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        mDecodeManager.onDestroy();
        super.onDestroy();
    }


    class UIClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.photo_take_img:
                    break;
            }
        }
    }

    public static class QrHandler extends Handler{
        public static final int DECODE_SUCCESS = 0x0011;
        public static final int DECODE_FAILED = 0x0021;
        public static final int DECODE_POSSIBLE = 0x0031;

        private WeakReference<QrDecodeFragment> reference;
        public QrHandler(QrDecodeFragment fragment){
            reference = new WeakReference(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            QrDecodeFragment fragment = reference.get();
            if ((fragment == null))
                return;
            switch (msg.what){
                case DECODE_FAILED:
                    break;
                case DECODE_SUCCESS:
//                    if(msg.obj != null)
                        Toast.makeText(fragment.getActivity(), "parse ok !", Toast.LENGTH_SHORT);
                    break;
                case DECODE_POSSIBLE: {
                    if (msg.obj != null){
                        ResultPoint resultPoint = (ResultPoint)msg.obj;
                        fragment.mMaskView.addPossibleResultPoint(resultPoint);
                    }
                    break;
                }
            }
        }
    }
}
