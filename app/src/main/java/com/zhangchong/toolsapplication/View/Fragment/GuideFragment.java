package com.zhangchong.toolsapplication.View.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhangchong.toolsapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.zhangchong.toolsapplication.View.Fragment.GuideFragment.GuideFragmentListener} interface
 * to handle interaction events.
 * Use the {@link GuideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideFragment extends Fragment {
    private GuideFragmentListener mListener;


    private ListView mListView;
    private BaseAdapter mListViewAdapter;
    private String[] mToolsName;

    public static GuideFragment newInstance(){
        GuideFragment fragment = new GuideFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValues();
    }

    public interface GuideFragmentListener {
        public void onItemClick(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initViews(inflater, container, savedInstanceState);
    }

    public void addText(String text){
        String[] temp = new String[mToolsName.length];
        for (int i = 0; i < temp.length; i++) {
            if(i == temp.length -1){
                temp[i] = text;
            }else {
                temp[i] = mToolsName[i];
            }
        }

        mToolsName = temp;
        mListViewAdapter.notifyDataSetChanged();
    }

    public View initViews(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guide, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listview_guide);
        mListViewAdapter = new ToolsAdapter();
        mListView.setAdapter(mListViewAdapter);
        ToolsItemClick click = new ToolsItemClick();
        mListView.setOnItemClickListener(click);

        return rootView;
    }

    public void initValues() {
        mToolsName = getResources().getStringArray(R.array.name_tools);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (GuideFragmentListener) activity;
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

    /**
     * Adapter
     */
    class ToolsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mToolsName.length;
        }

        @Override
        public Object getItem(int position) {
            return mToolsName[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_guide, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextView.setText(mToolsName[position]);

            return convertView;
        }

        class ViewHolder {
            public TextView mTextView;

            public ViewHolder(View convertView) {
                if (convertView == null)
                    return;
                mTextView = (TextView) convertView.findViewById(R.id.guide_item_title);
            }
        }
    }


    /**
     * ListView onclick
     */
    class ToolsItemClick implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mListener != null)
                mListener.onItemClick(position);
        }
    }

}
