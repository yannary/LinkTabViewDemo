package yannary.link.tab.widget;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyanyan on 2018/3/26.
 */

public class DateUtils {

    /**
     * 获取当前周日期信息
     *
     * @return
     */
    public static List<DayData> getWeekDate() {
        List<DayData> data = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar c = Calendar.getInstance();
        //获取当天星期几
        int mWay = c.get(Calendar.DAY_OF_WEEK);
        //将时间退到周日
        for (int i = 0; i < mWay; i++) {
            c.add(Calendar.DATE, -1);
        }
        //得到本周时间
        for (int i = 0; i < 7; i++) {
            c.add(Calendar.DATE, 1);
            c.get(Calendar.DAY_OF_MONTH);  // 日
//            data.add(new DayData(format.format(c.getTime()), c.get(Calendar.DAY_OF_MONTH), getWeekDay(c.get(Calendar.DAY_OF_WEEK), mWay),
//                    c.get(Calendar.DAY_OF_WEEK) == mWay ? true : false));
            data.add(new DayData(format.format(c.getTime()), c.get(Calendar.DAY_OF_MONTH), getWeekDay(c.get(Calendar.DAY_OF_WEEK), c),
                    format.format(c.getTime()).equals(format.format(Calendar.getInstance().getTime())) ? true : false));
        }
        return data;
    }

    /**
     * 获取指定日期一周信息
     *
     * @param dateStr 指定日期
     * @return 一周时间列表
     */
    public static List<DayData> getWeekDate(String dateStr) {
        List<DayData> data = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            Date date = format.parse(dateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            //获取当天星期几
            int mWay = c.get(Calendar.DAY_OF_WEEK);
            //将时间退到周日
            for (int i = 0; i < mWay; i++) {
                c.add(Calendar.DATE, -1);
            }
            //得到本周时间
            for (int i = 0; i < 7; i++) {
                c.add(Calendar.DATE, 1);
                c.get(Calendar.DAY_OF_MONTH);  // 日
                data.add(new DayData(format.format(c.getTime()), c.get(Calendar.DAY_OF_MONTH), getWeekDay(c.get(Calendar.DAY_OF_WEEK), c),
                        format.format(c.getTime()).equals(format.format(Calendar.getInstance().getTime())) ? true : false));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取指定天的星期
     *
     * @param day
     * @param c
     * @return
     */
    private static String getWeekDay(int day, Calendar c) {
        String s = null;
        switch (day) {
            case 1:
                s = "日";
                break;
            case 2:
                s = "一";
                break;
            case 3:
                s = "二";
                break;
            case 4:
                s = "三";
                break;
            case 5:
                s = "四";
                break;
            case 6:
                s = "五";
                break;
            case 7:
                s = "六";
                break;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        if (format.format(c.getTime()).equals(format.format(Calendar.getInstance().getTime()))) {
            s = "今";
        }
        return s;
    }

    /**
     * 获得指定日期的前intervalDays天
     *
     * @param specifiedDay 指定天
     * @param intervalDays 间隔天数
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay, int intervalDays) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy年MM月dd日").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - intervalDays);

        String dayBefore = new SimpleDateFormat("yyyy年MM月dd日").format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后intervalDays天
     *
     * @param specifiedDay 指定天
     * @param intervalDays 间隔天数
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay, int intervalDays) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy年MM月dd日").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + intervalDays);

        String dayAfter = new SimpleDateFormat("yyyy年MM月dd日").format(c.getTime());
        return dayAfter;
    }

    /**
     * 获得上周- 周日  的日期
     *
     * @return 上周周日日期
     */
    public static String getPreviousSunday() {
        int mondayPlus = getMondayPlus();
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DATE, mondayPlus - 1);
        String preMonday = new SimpleDateFormat("yyyy年MM月dd日").format(currentDate.getTime());
        return preMonday;
    }

    /**
     * 获得当前周- 周六  的日期
     *
     * @return 当前周周六日期
     */
    public static String getPreviousSaturday() {
        int mondayPlus = getMondayPlus();
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DATE, mondayPlus + 5);
        String preMonday = new SimpleDateFormat("yyyy年MM月dd日").format(currentDate.getTime());
        return preMonday;
    }

    // 获得本周一与当前日期相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    /**
     * 获取过去或者未来 任意天内的日期数组
     *
     * @param intervals intervals天内
     * @return 日期数组
     */
    public static ArrayList<String> getPastWeek(int intervals) {
        ArrayList<String> pastDaysList = new ArrayList<>();
        ArrayList<String> fetureDaysList = new ArrayList<>();
        for (int i = 0; i < intervals; i++) {
            pastDaysList.add(getPastDate(i));
            fetureDaysList.add(getFutureDate(i));
        }
        return pastDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }


    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFutureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }
}
