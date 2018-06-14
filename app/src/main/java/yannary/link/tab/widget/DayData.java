package yannary.link.tab.widget;

/**
 * Created by zhangyanyan on 2018/3/26.
 */

public class DayData {
    private String date; // 完整时间 yyyy年MM月dd日
    private int day; // 日期
    private String num; // 星期几
    private boolean isToday; // 是否为今天

    public DayData(String date, int day, String num, boolean isToday) {
        this.date = date;
        this.day = day;
        this.num = num;
        this.isToday = isToday;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }
}
