package acropollis.municipali.utls;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

    public String getDateText(Date date) {
        int monthDate = getDate(date);
        String month = getMonthText(getMonth(date));
        int year = getYear(date);

        return String.format(Locale.ENGLISH, "%s %d, %d", month, monthDate, year);
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

    public String getShortMonthText(int month) {
        switch (month) {
            case Calendar.JANUARY:
                return context.getResources().getString(R.string.january_short);
            case Calendar.FEBRUARY:
                return context.getResources().getString(R.string.february_short);
            case Calendar.MARCH:
                return context.getResources().getString(R.string.march_short);
            case Calendar.APRIL:
                return context.getResources().getString(R.string.april_short);
            case Calendar.MAY:
                return context.getResources().getString(R.string.may_short);
            case Calendar.JUNE:
                return context.getResources().getString(R.string.june_short);
            case Calendar.JULY:
                return context.getResources().getString(R.string.july_short);
            case Calendar.AUGUST:
                return context.getResources().getString(R.string.august_short);
            case Calendar.SEPTEMBER:
                return context.getResources().getString(R.string.september_short);
            case Calendar.OCTOBER:
                return context.getResources().getString(R.string.october_short);
            case Calendar.NOVEMBER:
                return context.getResources().getString(R.string.november_short);
            case Calendar.DECEMBER:
            default:
                return context.getResources().getString(R.string.december_short);
        }
    }

    public Date formatDate(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
