package acropollis.municipali.utls;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import acropollis.municipali.R;

@EBean
public class DateUtils {
    @RootContext
    Context context;

    public boolean isSameDate(Date date, Date today) {
        return
                getYear(date) == getYear(today) &&
                getMonth(date) == getMonth(today) &&
                getDate(date) == getDate(today);
    }

    public int getYear(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);

        return c.get(Calendar.YEAR);
    }

    public int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal.get(Calendar.MONTH);
    }

    public int getDate(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);

        return c.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);

        return c.get(Calendar.DAY_OF_WEEK);
    }

    public int getDistanceInDays(Date d1, Date d2) {
        return (int) TimeUnit.DAYS.convert(cropToDate(d2).getTime() - cropToDate(d1).getTime(), TimeUnit.MILLISECONDS);
    }

    public String getDayOfWeekText(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return context.getResources().getString(R.string.monday);
            case Calendar.TUESDAY:
                return context.getResources().getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return context.getResources().getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return context.getResources().getString(R.string.thursday);
            case Calendar.FRIDAY:
                return context.getResources().getString(R.string.friday);
            case Calendar.SATURDAY:
                return context.getResources().getString(R.string.saturday);
            case Calendar.SUNDAY:
            default:
                return context.getResources().getString(R.string.sunday);
        }
    }

    public String getMonthText(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return context.getResources().getString(R.string.january);
            case Calendar.FEBRUARY:
                return context.getResources().getString(R.string.february);
            case Calendar.MARCH:
                return context.getResources().getString(R.string.march);
            case Calendar.APRIL:
                return context.getResources().getString(R.string.april);
            case Calendar.MAY:
                return context.getResources().getString(R.string.may);
            case Calendar.JUNE:
                return context.getResources().getString(R.string.june);
            case Calendar.JULY:
                return context.getResources().getString(R.string.july);
            case Calendar.AUGUST:
                return context.getResources().getString(R.string.august);
            case Calendar.SEPTEMBER:
                return context.getResources().getString(R.string.september);
            case Calendar.OCTOBER:
                return context.getResources().getString(R.string.october);
            case Calendar.NOVEMBER:
                return context.getResources().getString(R.string.november);
            case Calendar.DECEMBER:
            default:
                return context.getResources().getString(R.string.december);
        }
    }

    private Date cropToDate(Date d) {
        Calendar c = Calendar.getInstance();

        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }
}
