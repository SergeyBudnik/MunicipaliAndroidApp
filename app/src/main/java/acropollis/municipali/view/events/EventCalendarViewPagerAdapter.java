package acropollis.municipali.view.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import acropollis.municipali.data.article.TranslatedArticle;
import acropollis.municipali.utls.DateUtils;
import lombok.Getter;
import lombok.Setter;

class EventCalendarViewPagerAdapter extends FragmentPagerAdapter {
    @Getter @Setter
    private static class MonthAndYear {
        private int month;
        private int year;

        MonthAndYear(int month, int year) {
            this.month = month;
            this.year = year;
        }
    }

    public static final int MONTHS_AMOUNT = 5;

    private CalendarMonthFragment [] calendarMonthsFragments = new CalendarMonthFragment [0];

    EventCalendarViewPagerAdapter(
            FragmentManager fm,
            Date today,
            DateUtils dateUtils,
            List<TranslatedArticle> events,
            EventCalendarFragment.SpecialDateOnClickListener specialDateOnClickListener) {

        super(fm);


        MonthAndYear current = new MonthAndYear(
                dateUtils.getMonth(today),
                dateUtils.getYear(today)
        );

        MonthAndYear [] monthAndYears = new MonthAndYear[MONTHS_AMOUNT];

        monthAndYears[MONTHS_AMOUNT / 2] = current;

        for (int i = 1; i <= MONTHS_AMOUNT / 2; i++) {
            monthAndYears[MONTHS_AMOUNT / 2 - i] = getPrevMonthAndYear(current, i);
            monthAndYears[MONTHS_AMOUNT / 2 + i] = getNextMonthAndYear(current, i);
        }

        calendarMonthsFragments = new CalendarMonthFragment [MONTHS_AMOUNT];

        for (int i = 0; i < MONTHS_AMOUNT; i++) {
            calendarMonthsFragments[i] = CalendarMonthFragment_.builder().build();

            calendarMonthsFragments[i].setEvents(
                    monthAndYears[i].getMonth(),
                    monthAndYears[i].getYear(),
                    events,
                    specialDateOnClickListener);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return calendarMonthsFragments[position];
    }

    @Override
    public int getCount() {
        return MONTHS_AMOUNT;
    }

    private MonthAndYear getPrevMonthAndYear(MonthAndYear current, int depth) {
        MonthAndYear o = current;

        for (int i = 0; i < depth; i++) {
            if (o.getMonth() != Calendar.JANUARY) {
                o = new MonthAndYear(o.getMonth() - 1, o.getYear());
            } else {
                o = new MonthAndYear(Calendar.DECEMBER, o.getYear() - 1);
            }
        }

        return o;
    }

    private MonthAndYear getNextMonthAndYear(MonthAndYear current, int depth) {
        MonthAndYear o = current;

        for (int i = 0; i < depth; i++) {
            if (o.getMonth() != Calendar.DECEMBER) {
                o = new MonthAndYear(o.getMonth() + 1, o.getYear());
            } else {
                o = new MonthAndYear(Calendar.JANUARY, o.getYear() + 1);
            }
        }

        return o;
    }
}
