package yannary.link.tab;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import yannary.link.tab.widget.DayData;
import yannary.link.tab.widget.LinkTabView;
import yannary.link.tab.widget.PagerBottomAdapter;

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
            mFragments.add(SimpleCardFragment.getInstance(""));
        }
        mLinkTabView = (LinkTabView) findViewById(R.id.link_layout);
        mBottomVp = (ViewPager) findViewById(R.id.link_bottom_vp);
        mLinkTabView.setSelectedListener(this); // setSelectedListener要放在setViewPager前面
        mLinkTabView.setViewPager(mBottomVp, this, mFragments);
        mLinkTabView.setLimitWeek(5, 2);

    }

    @Override
    public void onPagerBottomSelected(int position, DayData dayData) {
        SimpleCardFragment cardFragment = (SimpleCardFragment) mFragments.get(position);
        if (cardFragment != null && dayData != null) {
            cardFragment.updateUI(dayData.getDate());
        }
    }
}
