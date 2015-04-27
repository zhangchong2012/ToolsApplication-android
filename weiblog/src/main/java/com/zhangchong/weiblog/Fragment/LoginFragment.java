package com.zhangchong.weiblog.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zhangchong.weiblog.R;

import org.w3c.dom.Text;

/**
 * @author zhangchong
 */
public class LoginFragment extends android.support.v4.app.Fragment {
    public static final String TAG = LoginFragment.class.getSimpleName();
    private OnFragmentLoginListener mListener;

    private EditText mNameInput;
    private EditText mPwdInput;
    private TextView mRegisterView;
    private TextView mLoginView;

    public static LoginFragment newInstance(){
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initViews(inflater, container, savedInstanceState);
        return view;
    }

    private View initViews(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mNameInput = (EditText)rootView.findViewById(R.id.login_name_input);
        mPwdInput = (EditText)rootView.findViewById(R.id.login_pwd_input);
        mRegisterView = (TextView)rootView.findViewById(R.id.button_register);
        mLoginView = (TextView)rootView.findViewById(R.id.button_login);

        UIClick click = new UIClick();
        mRegisterView.setOnClickListener(click);
        mLoginView.setOnClickListener(click);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentLoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentLoginListener {
        public void onLogin(String name, String pwd);
        public void onRegister();
    }


    class UIClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_login:
                    if(mListener != null)
                        mListener.onLogin(mNameInput.getText().toString(), mPwdInput.getText().toString());
                    break;
                case R.id.button_register:
                    if(mListener != null)
                        mListener.onRegister();
                    break;
                default:
                    break;
            }
        }
    }
}
