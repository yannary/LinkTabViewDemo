package yannary.link.tab.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yannary.link.tab.R;

/**
 * Created by zhangyanyan on 2018/3/26.
 */

public class LinkTabView extends LinearLayout implements PagerTopAdapter.PagerItemClickListener {

    private String TAG = this.getClass().getSimpleName();

    /**
     * 顶部标题栏ViewPager
     */
    private ViewPager mTopVp;
    private PagerTopAdapter mTopPagerAdapter;
    private List<List<DayData>> mTitleList = new ArrayList<>();
    private String[] mTitles = new String[7];

    private boolean left = false;  // 左滑
    private boolean right = false;  // 右滑
    private boolean isScrolling = false; // 是否正在滑动
    private int lastValue = -1;  // 记录上一次位置偏移量的像素值

    /**
     * 底部ViewPager
     */
    private ViewPager mBottomVp;
    private int mBottomSelectedPosition = 0;
    private PagerBottomAdapter.PagerItemSelectedListener mBottomVpListener;

    /**
     * 常规属性
     */
    private int mFutureWeek = 1; // 未来1周
    private int mPastWeek = 40; // 之前40周
    private List<DayData> mCurrentWeek = new ArrayList<>();

    public LinkTabView(Context context) {
        this(context, null, 0);
    }

    public LinkTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinkTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_link, this);
        initData();
        initTopView(context);
    }

    private void initData() {
        List<DayData> currentWeek = DateUtils.getWeekDate();
        if (!currentWeek.isEmpty()) {
            mCurrentWeek.clear();
            mCurrentWeek.addAll(currentWeek);
            String beforeDate = DateUtils.getSpecifiedDayBefore(currentWeek.get(0).getDate(), 1);
            String afterDate = DateUtils.getSpecifiedDayAfter(currentWeek.get(currentWeek.size() - 1).getDate(), 1);
            mTitleList.add(DateUtils.getWeekDate(beforeDate));
            mTitleList.add(currentWeek);
            mTitleList.add(DateUtils.getWeekDate(afterDate));
            for (int i = 0; i < currentWeek.size(); i++) {
                DayData dayData = currentWeek.get(i);
                if (dayData.isToday()) {
                    mBottomSelectedPosition = i;
                }
                mTitles[i] = dayData.getDate();
            }
        }
    }

    private void initTopView(Context context) {
        mTopVp = (ViewPager) findViewById(R.id.item_link_top_vp);
        mTopPagerAdapter = new PagerTopAdapter(context, mTitleList);
        mTopVp.setAdapter(mTopPagerAdapter);
        mTopPagerAdapter.setPagerTopItemClickListener(this);
        mTopVp.setCurrentItem(1);
        mTopVp.removeOnPageChangeListener(mTopPageChangeListener);
        mTopVp.addOnPageChangeListener(mTopPageChangeListener);
        mTopVp.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
                viewPager.setAdapter(newAdapter);
            }
        });
    }

    ViewPager.OnPageChangeListener mTopPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (isScrolling) {
                if (lastValue > positionOffsetPixels) {
                    // 递减，向右侧滑动
                    right = true;
                    left = false;
                } else if (lastValue < positionOffsetPixels) {
                    // 递增，向左侧滑动
                    right = false;
                    left = true;
                } else if (lastValue == positionOffsetPixels) {
                    right = left = false;
                }
            }
            lastValue = positionOffsetPixels;
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // SCROLL_STATE_DRAGGING 1：开始滑动
            // SCROLL_STATE_SETTLING 2：滑动结束
            // SCROLL_STATE_IDLE     0：什么都没做
            // 从滑动开始依次为：state== (1,2,0)
            isScrolling = state == ViewPager.SCROLL_STATE_DRAGGING;
            if (state == ViewPager.SCROLL_STATE_SETTLING) {

                if (right) { // 右滑加载左侧数据
                    loadLeftData2TopViewPager();
                }
                if (left) {  // 左滑加载右侧数据
                    loadRightData2TopViewPager();
                }
                right = left = false;
            }
        }
    };

    /**
     * 顶部ViewPager
     * 右滑,加载左侧数据
     */
    private void loadLeftData2TopViewPager() {
        List<DayData> currentWeek = mTitleList.get(0);
        if (!currentWeek.isEmpty()) {
            mCurrentWeek.clear();
            mCurrentWeek.addAll(currentWeek);
            List<DayData> beforeWeek = DateUtils.getWeekDate(DateUtils.getSpecifiedDayBefore(
                    currentWeek.get(0).getDate(), 1));
            List<DayData> afterWeek = DateUtils.getWeekDate(DateUtils.getSpecifiedDayAfter(
                    currentWeek.get(currentWeek.size() - 1).getDate(), 1));
            mTitleList.clear();
            String limitPastDay = DateUtils.getSpecifiedDayBefore(DateUtils.getPreviousSunday(), mPastWeek * 7);
            String pastDay = currentWeek.get(0).getDate();
            if (pastDay.compareTo(limitPastDay) > 0) {
                mTitleList.add(beforeWeek);
                mTitleList.add(currentWeek);
                mTitleList.add(afterWeek);
                refreshCurrentItem(1);
            } else {
                mTitleList.add(currentWeek);
                mTitleList.add(afterWeek);
                refreshCurrentItem(0);
            }
        }
        /*int currentItem = mTopVp.getCurrentItem();
        Log.d(TAG, "右滑,currentItem:" + currentItem);
        List<DayData> currentWeek = mTitleList.get(0);
        List<DayData> beforeWeek = DateUtils.getWeekDate(DateUtils.getSpecifiedDayBefore(currentWeek.get(0).getDate(), 1));
        mTitleList.add(0, beforeWeek);
        mTitleList.remove(mTitleList.size() - 1);

        mTopVp.post(new Runnable() {
            @Override
            public void run() {
                mTopPagerAdapter.clearViewList();
                mTopPagerAdapter.notifyDataSetChanged();
                mTopVp.setCurrentItem(1, true);
                mTopPagerAdapter.setSelectedViewBackground(mBottomSelectedPosition);
                if (mBottomVp != null) {
                    mBottomVp.setCurrentItem(mBottomSelectedPosition);
                }
            }
        });*/
    }

    /**
     * 顶部ViewPager
     * 左滑,加载右侧数据
     */
    private void loadRightData2TopViewPager() {
        List<DayData> currentWeek = mTitleList.get(mTitleList.size() - 1);
        if (!currentWeek.isEmpty()) {
            mCurrentWeek.clear();
            mCurrentWeek.addAll(currentWeek);
            List<DayData> beforeWeek = DateUtils.getWeekDate(DateUtils.getSpecifiedDayBefore(
                    currentWeek.get(0).getDate(), 1));
            List<DayData> beforeWeek2 = DateUtils.getWeekDate(DateUtils.getSpecifiedDayBefore(
                    beforeWeek.get(0).getDate(), 1));
            List<DayData> afterWeek = DateUtils.getWeekDate(DateUtils.getSpecifiedDayAfter(
                    currentWeek.get(currentWeek.size() - 1).getDate(), 1));
            mTitleList.clear();
            String limitFutureDay = DateUtils.getSpecifiedDayAfter(
                    DateUtils.getPreviousSaturday(), mFutureWeek * 7);
            String futureDay = currentWeek.get(currentWeek.size() - 1).getDate();
            if (futureDay.compareTo(limitFutureDay) < 0) {
                mTitleList.add(beforeWeek);
                mTitleList.add(currentWeek);
                mTitleList.add(afterWeek);
            } else if (futureDay.compareTo(limitFutureDay) == 0) {
                mTitleList.add(beforeWeek);
                mTitleList.add(currentWeek);
            } else {
                mTitleList.add(beforeWeek2);
                mTitleList.add(beforeWeek);
            }
        }
        refreshCurrentItem(1);
        /*List<DayData> currentWeek = mTitleList.get(mTitleList.size() - 1);
            if (!currentWeek.isEmpty()) {
                String afterDate = DateUtils.getSpecifiedDayAfter(currentWeek.get(currentWeek.size() - 1).getDate(), 1);
                mTitleList.add(DateUtils.getWeekDate(afterDate));
                mTitleList.remove(0);
                mTopVp.post(new Runnable() {
                    @Override
                    public void run() {
                        mTopPagerAdapter.clearViewList();
                        mTopPagerAdapter.notifyDataSetChanged();
                        int current = mTopVp.getCurrentItem();
                        mTopVp.setCurrentItem(1, true);
                        mTopPagerAdapter.setSelectedViewBackground(mBottomSelectedPosition);
                        if (mBottomVp != null) {
                            mBottomVp.setCurrentItem(mBottomSelectedPosition);
                        }
                    }
                });
            }*/

    }

    /**
     * 刷新顶部ViewPager当前页面
     *
     * @param item item index to select
     */
    private void refreshCurrentItem(final int item) {
        mTopVp.post(new Runnable() {
            @Override
            public void run() {
                mTopPagerAdapter.clearViewList();
                mTopPagerAdapter.notifyDataSetChanged();
                mTopVp.setCurrentItem(item, true);
                mTopPagerAdapter.setSelectedViewBackground(mBottomSelectedPosition);
                if (mBottomVp != null) {
                    mBottomVp.setCurrentItem(mBottomSelectedPosition);
                }
                setBottomViewPagerListener();
            }
        });
    }

    ViewPager.OnPageChangeListener mBottomChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mBottomSelectedPosition = position;
        }

        @Override
        public void onPageSelected(int position) {
            mBottomSelectedPosition = position;
            if (mTopPagerAdapter != null) {
                mTopPagerAdapter.setSelectedViewBackground(mBottomSelectedPosition);
            }
            setBottomViewPagerListener();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mBottomSelectedPosition == 6 && state == 1) {
                if (mTopPagerAdapter != null) {
                    mTopPagerAdapter.setSelectedViewBackground(mBottomSelectedPosition);
                }
            }
        }
    };

    /**
     * 设置底部ViewPager监听
     */
    private void setBottomViewPagerListener() {
        if (!mCurrentWeek.isEmpty() && mBottomVpListener != null) {
            DayData dayData = mCurrentWeek.get(mBottomSelectedPosition);
            mBottomVpListener.onPagerBottomSelected(mBottomSelectedPosition, dayData);
        }
    }

    @Override
    public void onPagerTopItemClick(View view, int position, DayData dayData) {
        mBottomSelectedPosition = position;
        if (mBottomVp != null) {
            mBottomVp.setCurrentItem(position);
        }
        TextView tv = (TextView) view;
        if (tv.getText().toString().equals("今")) {
            tv.setBackgroundResource(R.color.link_selected_today_bg);
        } else {
            tv.setBackgroundResource(R.color.link_selected_bg);
        }
    }

    public void setViewPager(ViewPager viewPager, FragmentActivity fragmentActivity, ArrayList<Fragment> fragments) {
        if (viewPager == null) {
            throw new IllegalStateException("ViewPager can not be NULL !");
        }
        if (fragmentActivity == null)
            return;
        if (fragments.isEmpty())
            return;
        this.mBottomVp = viewPager;
        viewPager.setAdapter(new PagerBottomAdapter(fragmentActivity.getSupportFragmentManager(), fragments, mTitles));
        viewPager.setOffscreenPageLimit(7);
        viewPager.removeOnPageChangeListener(mBottomChangeListener);
        viewPager.addOnPageChangeListener(mBottomChangeListener);
        viewPager.setCurrentItem(mBottomSelectedPosition);
    }

    /**
     * 设置前后最大天数，以 周 为单位
     *
     * @param pastWeek   过去最大周数
     * @param futureWeek 未来多大周数
     */
    public void setLimitWeek(int pastWeek, int futureWeek) {
        if (pastWeek > 0) {
            this.mPastWeek = pastWeek;
        }
        if (futureWeek > 0) {
            this.mFutureWeek = futureWeek;
        }
    }

    public void setSelectedListener(PagerBottomAdapter.PagerItemSelectedListener bottomVpListener) {
        this.mBottomVpListener = bottomVpListener;
    }

}
