package com.zhangchong.toolsapplication.Partner.Camera.TakePhoto;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.zhangchong.toolsapplication.Partner.Camera.CameraActivity;
import com.zhangchong.toolsapplication.R;

public class TakePhotoFragment extends Fragment {
    public static final String TAG = "TakePhotoFragment";
    private OnTakePhotoListener mListener;
    private SurfaceView mCameraView;
    private PictureSaveTread.SaveHandler mSaveHandler;

    public static TakePhotoFragment newInstance() {
        TakePhotoFragment fragment = new TakePhotoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public TakePhotoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PictureSaveTread thread = new PictureSaveTread();
        thread.start();
        mSaveHandler = thread.getHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_photo, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View rootView){
        UIClick click = new UIClick();
        mCameraView = (SurfaceView)rootView.findViewById(R.id.photo_preview);
        ((CameraActivity)getActivity()).getCameraManager().initCameraSurface(mCameraView);
        rootView.findViewById(R.id.photo_take_img).setOnClickListener(click);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnTakePhotoListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnTakePhotoListener {
        public void onTakePhoto();
    }


    class UIClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.photo_take_img:
                    CameraPhotoCallBack callBack = new CameraPhotoCallBack();
                    // takePicture()方法需要传入三个监听参数
                    // 第一个监听器；当用户按下快门时激发该监听器
                    // 第二个监听器；当相机获取原始照片时激发该监听器
                    // 第三个监听器；当相机获取JPG照片时激发该监听器
                    // JPG图像
                    ((CameraActivity)getActivity()).getCameraManager().getCamera().takePicture(callBack,
                            null, callBack, null);
                    break;
            }
        }
    }
}
