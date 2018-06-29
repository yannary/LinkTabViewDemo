package yannary.link.tab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleCardFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private TextView mTitleTv;

    private boolean isViewCreated = false; // 视图是否初始化完成
    private String mTitle;


    public static SimpleCardFragment getInstance() {
        SimpleCardFragment sf = new SimpleCardFragment();
        return sf;
    }

    public void setParams(boolean isTopSlide, String title) {
        Log.w(TAG, "setParams:" + title);
        mTitle = title;
        if (isTopSlide) {
            setUserVisibleHint(true);
        }
    }

    public void updateUI() {
        if (!isViewCreated) {
            Log.w(TAG, "updateUI,SimpleCardFragment is not init");
            return;
        }
        if (!getUserVisibleHint()) {
            Log.d("test", "updateUI,getUserVisibleHint :" + getUserVisibleHint());
            return;
        }
        mTitleTv.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
        mTitleTv.setText(mTitle + "");
        Log.w(TAG, "updateUI,successful:" + mTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_simple_card, null);
        mTitleTv = (TextView) v.findViewById(R.id.card_title_tv);
        isViewCreated = true;
        updateUI();
        Log.w(TAG, "onCreateView:updateUI");
        return v;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (TextUtils.isEmpty(mTitle))
//            return;
//        outState.putString("title", mTitle);
//        Log.d("test", "onSaveInstanceState,title:" + mTitle);
//    }
//
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        if (savedInstanceState == null) {
//            return;
//        }
//        String title = savedInstanceState.getString("title");
//        if (TextUtils.isEmpty(title)) {
//            Log.d(TAG, "onViewStateRestored,title is null");
//            return;
//        }
//        mTitle = title;
//        Log.d(TAG, "onViewStateRestored,title:" + mTitle);
//    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.w(TAG, "setUserVisibleHint,updateUI:" + mTitle);
        updateUI();
    }
}