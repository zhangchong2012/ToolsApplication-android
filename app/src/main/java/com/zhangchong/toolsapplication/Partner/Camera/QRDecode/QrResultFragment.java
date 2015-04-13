package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangchong.toolsapplication.Partner.Camera.ActivityCallback;
import com.zhangchong.toolsapplication.R;

public class QrResultFragment extends Fragment {
    public static final String TAG = QrResultFragment.class.getSimpleName();
    private static final String PAGE_TYPE = "type";
    public static final int PAGE_SCAN_RESULT = 0x001;
    public static final int PAGE_create_RESULT = 0x002;

    private static final String PARAM_RESULT = "result";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_FACTOR = "factor";

    private int mPageType;
    private ActivityCallback mListener;

    private ImageView mImageView;
    private TextView mButtonBack;
    private TextView mButtonShare;
    private TextView mButtonBook;
    private TextView mButtonGoon;

    private String mResult;
    private byte[] mData;
    private float mFactor;

    public static QrResultFragment newInstance(int pageType, String result, byte[] data, float factor) {
        QrResultFragment fragment = new QrResultFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_TYPE, pageType);
        args.putString(PARAM_RESULT, result);
        args.putByteArray(PARAM_DATA, data);
        args.putFloat(PARAM_FACTOR, factor);
        fragment.setArguments(args);
        return fragment;
    }

    public QrResultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPageType = args.getInt(PAGE_TYPE);
            mResult = args.getString(PARAM_RESULT);
            mData = args.getByteArray(PARAM_DATA);
            mFactor = args.getFloat(PARAM_FACTOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return initViews(inflater, container, savedInstanceState);
    }

    private View initViews(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr_result, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.qr_img);
        mButtonBack = (TextView) rootView.findViewById(R.id.button_back);
        mButtonBook = (TextView) rootView.findViewById(R.id.button_book);
        mButtonGoon = (TextView) rootView.findViewById(R.id.button_goon_scan);
        mButtonShare = (TextView) rootView.findViewById(R.id.button_share);

        UIClick click = new UIClick();
        mButtonBack.setOnClickListener(click);
        mButtonShare.setOnClickListener(click);
        mButtonBook.setOnClickListener(click);
        mButtonGoon.setOnClickListener(click);

        Bitmap bmp = BitmapFactory.decodeByteArray(mData, 0, mData.length, null);
        mImageView.setImageBitmap(bmp);
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ActivityCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    class UIClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_back:
                    break;
                case R.id.button_book:
                    break;
                case R.id.button_goon_scan:
                    break;
                case R.id.button_share:
                    break;
                default:
                    break;
            }
        }
    }

}
