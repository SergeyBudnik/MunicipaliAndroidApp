package acropollis.municipali.view.calendar;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import acropollis.municipali.utls.DateUtils;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import lombok.Getter;
import lombok.Setter;

@EBean
class CalendarViewPagerAdapter extends PagerAdapter {
    @Getter @Setter
    private static class MonthAndYear {
        private int month;
        private int year;

        MonthAndYear(int month, int year) {
            this.month = month;
            this.year = year;
        }
    }

    static final int MONTHS_AMOUNT = 5;

    @Bean
    DateUtils dateUtils;

    private Context context;

    private Date startDateSelected = null;
    private Date endDateSelected = null;

    private CalendarSelectionIntervalListener calendarSelectionIntervalListener;
    private CalendarMonthView[] calendarMonthsViews = new CalendarMonthView[0];

    CalendarViewPagerAdapter(Context context) {
        this.context = context;
    }

    void init(
            CalendarSelectionIntervalListener calendarSelectionIntervalListener,
            List<TranslatedArticle> events,
            Date startDateSelected, Date endDateSelected
    ) {
        this.startDateSelected = startDateSelected;
        this.endDateSelected = endDateSelected;

        this.calendarSelectionIntervalListener = calendarSelectionIntervalListener;

        Date today = new Date();

        MonthAndYear current = new MonthAndYear(dateUtils.getMonth(today), dateUtils.getYear(today));
        MonthAndYear [] monthAndYears = new MonthAndYear[MONTHS_AMOUNT];

        monthAndYears[MONTHS_AMOUNT / 2] = current;

        for (int i = 1; i <= MONTHS_AMOUNT / 2; i++) {
            monthAndYears[MONTHS_AMOUNT / 2 - i] = getPrevMonthAndYear(current, i);
            monthAndYears[MONTHS_AMOUNT / 2 + i] = getNextMonthAndYear(current, i);
        }

        calendarMonthsViews = new CalendarMonthView[MONTHS_AMOUNT];

        for (int i = 0; i < MONTHS_AMOUNT; i++) {
            calendarMonthsViews[i] = CalendarMonthView_.build(context);

            calendarMonthsViews[i].init(
                    this,
                    monthAndYears[i].getMonth(),
                    monthAndYears[i].getYear(),
                    events
            );

            calendarMonthsViews[i].setSelectedDates(startDateSelected, endDateSelected);
        }
    }

    View getItem(int position) {
        return calendarMonthsViews[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = calendarMonthsViews[position];

        container.addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return MONTHS_AMOUNT;
    }

    void onDateClicked(Date date) {
        boolean intervalNotSelected = startDateSelected == null && endDateSelected == null;
        boolean intervalSelected = startDateSelected != null && endDateSelected != null;
        boolean beforeInterval = startDateSelected != null && startDateSelected.after(date);
        boolean dateEquals = startDateSelected != null && startDateSelected.equals(date);

        if (intervalNotSelected || intervalSelected) {
            endDateSelected = null;
            startDateSelected = date;
        } else if (beforeInterval) {
            endDateSelected = startDateSelected;
            startDateSelected = date;
        } else if (dateEquals) {
            startDateSelected = null;
        } else {
            endDateSelected = date;
        }

        calendarSelectionIntervalListener.onCalendarSelectionIntervalChanged(startDateSelected, endDateSelected);

        Stream.of(calendarMonthsViews).forEach(it -> it.setSelectedDates(startDateSelected, endDateSelected));
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
