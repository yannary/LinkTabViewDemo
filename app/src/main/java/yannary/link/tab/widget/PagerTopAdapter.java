package yannary.link.tab.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yannary.link.tab.R;

/**
 * Created by ton on 2018/3/24.
 */

public class PagerTopAdapter extends PagerAdapter {

    private Context context;
    private List<List<DayData>> mTitleList;
    private PagerItemClickListener mPagerItemClickListener;
    private List<TextView> mViewList = new ArrayList<>();


    public PagerTopAdapter(Context context, List<List<DayData>> titleList) {
        this.context = context;
        this.mTitleList = titleList;
    }

    @Override
    public int getCount() {
        return mTitleList.isEmpty() ? 0 : mTitleList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_link_top, container, false);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.item_top_ly);
        if (!mTitleList.isEmpty()) {
            List<DayData> dayDataList = mTitleList.get(position);
            linearLayout.removeAllViews();
            for (int i = 0; i < dayDataList.size(); i++) {
                final DayData dayData = dayDataList.get(i);
                TextView textView = new TextView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                lp.setMargins(4, 8, 4, 10);
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(context.getResources().getColor(R.color.link_text_color));
                mViewList.add(textView);
                boolean isToday = dayData.isToday();
                if (isToday) {
                    textView.setText("今");
                    textView.setBackgroundResource(R.color.link_selected_today_bg);
                } else {
                    textView.setText(String.valueOf(dayData.getDay()));
                }

                final int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPagerItemClickListener != null) {
                            mPagerItemClickListener.onPagerTopItemClick(v, finalI, dayData);
                        }
                    }
                });

                linearLayout.addView(textView);
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void clearViewList() {
        if (!mViewList.isEmpty()) {
            mViewList.clear();
        }
    }

    /**
     * 设置选中标题的背景色
     *
     * @param selectedPosition
     */
    public void setSelectedViewBackground(int selectedPosition) {
        if (!mViewList.isEmpty()) {
            for (TextView textView : mViewList) {
                textView.setBackgroundResource(0);
            }
            for (int i = 0; i < mViewList.size() / 7 + 1; i++) {
                selectedPosition = selectedPosition + i * 7;
                setViewBackground(selectedPosition);
            }
        }
    }

    private void setViewBackground(int selectedPosition) {
        if (mViewList.size() > selectedPosition) {
            TextView tv = mViewList.get(selectedPosition);
            if (tv.getText().toString().equals("今")) {
                tv.setBackgroundResource(R.color.link_selected_today_bg);
            } else {
                tv.setBackgroundResource(R.color.link_selected_bg);
            }
        }

    }

    public void setPagerTopItemClickListener(PagerItemClickListener pagerItemClickListener) {
        this.mPagerItemClickListener = pagerItemClickListener;
    }

    public interface PagerItemClickListener {
        void onPagerTopItemClick(View view, int position, DayData dayData);
    }

}
