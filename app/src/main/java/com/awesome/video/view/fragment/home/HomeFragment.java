package com.awesome.video.view.fragment.home;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.awesome.video.R;
import com.awesome.video.adapter.CourseAdapter;
import com.awesome.video.module.recommand.BaseRecommandModel;
import com.awesome.video.view.fragment.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * created by Alice on 2020/3/19
 * Description:
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "HomeFragment";
    /**
     * UI
     */
    private View mContentView;
    private ListView mListView;
    private TextView mQRCodeView;
    private TextView mCategoryView;
    private TextView mSearchView;
    private ImageView mLoadingView;
    /**
     * data
     */
    private CourseAdapter mAdapter;
    private BaseRecommandModel mRecommandData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestRecommandData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mQRCodeView = (TextView) mContentView.findViewById(R.id.qrcode_view);
        mQRCodeView.setOnClickListener(this);
        mCategoryView = (TextView) mContentView.findViewById(R.id.category_view);
        mCategoryView.setOnClickListener(this);
        mSearchView = (TextView) mContentView.findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);

        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //发送推荐产品请求
    private void requestRecommandData() {
//        RequestCenter.requestRecommandData(new DisposeDataListener() {
//            @Override
//            public void onSuccess(Object responseObj) {
//                mRecommandData = (BaseRecommandModel) responseObj;
//                //更新UI
//                showSuccessView();

//            }

//
//            @Override
//            public void onFailure(Object reasonObj) {
//
//            }
//        });


    }
//
//    //显示请求成功的UI
//    private void showSuccessView() {
//        mRecommandData=new BaseRecommandModel();
//        mRecommandData.data=new RecommandModel();
//        mRecommandData.data.list=new ArrayList<RecommandBodyValue>();
//        mRecommandData.data.list.add(new RecommandBodyValue)
//        if (mRecommandData.data.list != null && mRecommandData.data.list.size() > 0) {
//            mLoadingView.setVisibility(View.GONE);
//            mListView.setVisibility(View.VISIBLE);
//        }
//
//
//    }
}
