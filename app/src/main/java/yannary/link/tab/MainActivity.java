package yannary.link.tab;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import yannary.link.tab.widget.DayData;
import yannary.link.tab.widget.LinkTabView;
import yannary.link.tab.widget.PagerBottomAdapter;

/**
 * 没有对屏幕旋转做适配
 * AndroidManifest.xml中需加入android:screenOrientation="portrait"
 * 否则SimpleCardFragment中数据刷新会失败
 */
public class MainActivity extends AppCompatActivity implements PagerBottomAdapter.PagerItemSelectedListener {

    private String TAG = this.getClass().getSimpleName();
    private LinkTabView mLinkTabView;
    private ViewPager mBottomVp;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
    }

    private void initTab() {
        for (int i = 0; i < 7; i++) {
            mFragments.add(SimpleCardFragment.getInstance());
        }
        mLinkTabView = (LinkTabView) findViewById(R.id.link_layout);
        mBottomVp = (ViewPager) findViewById(R.id.link_bottom_vp);
        mLinkTabView.setSelectedListener(this); // setSelectedListener要放在setViewPager前面
        mLinkTabView.setViewPager(mBottomVp, getSupportFragmentManager(), mFragments);
        mLinkTabView.setLimitWeek(5, 2);

    }

    @Override
    public void onPagerBottomSelected(boolean isTopSlide, int position, DayData dayData) {
        SimpleCardFragment cardFragment = (SimpleCardFragment) mFragments.get(position);
        if (cardFragment != null && dayData != null) {
            cardFragment.setParams(isTopSlide, dayData.getDate());
            Log.d(TAG, "onPagerBottomSelected:" + position);
        }
    }
}
