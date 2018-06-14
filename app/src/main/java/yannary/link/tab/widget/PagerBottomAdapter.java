package yannary.link.tab.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyanyan on 2017/6/12.
 */

public class PagerBottomAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<>();
    private String[] mTitleList;

    public PagerBottomAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titleList) {
        super(fm);
        mFragmentList = fragmentList;
        mTitleList = titleList;
    }

    @Override
    public int getCount() {
        return mFragmentList.isEmpty() ? 0 : mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public interface PagerItemSelectedListener {
        void onPagerBottomSelected(int position, DayData dayData);
    }
}