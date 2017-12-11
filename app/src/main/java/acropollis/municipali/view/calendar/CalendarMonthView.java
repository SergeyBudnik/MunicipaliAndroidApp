package acropollis.municipali.view.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import acropollis.municipali.R;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipali.utls.DateUtils;

@EViewGroup(R.layout.fragment_calendar_month)
public class CalendarMonthView extends RelativeLayout {
    private static final int DAYS_IN_CALENDAR = 35;

    @ViewById(R.id.calendar_grid)
    GridView calendarGridView;

    @Bean
    DateUtils dateUtils;

    private CalendarViewPagerAdapter adapter;

    private Calendar currentDate = Calendar.getInstance();

    private int month;
    private int year;

    private Date startDateSelected = null;
    private Date endDateSelected = null;

    private CalendarAdapter calendarAdapter;

    public CalendarMonthView(Context context) {
        super(context);
    }

    public CalendarMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(
            CalendarViewPagerAdapter adapter,
            int month,
            int year,
            List<TranslatedArticle> events
    ) {
        this.adapter = adapter;
        this.month = month;
        this.year = year;

        updateCalendar(month, year, events);
    }

    public void setSelectedDates(Date startDateSelected, Date endDateSelected) {
        this.startDateSelected = startDateSelected;
        this.endDateSelected = endDateSelected;

        if (calendarAdapter != null) {
            calendarAdapter.notifyDataSetChanged();
        }
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void updateCalendar(int month, int year, List<TranslatedArticle> events) {
        this.month = month;
        this.year = year;

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        while (cells.size() < DAYS_IN_CALENDAR) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarGridView.setAdapter(calendarAdapter = new CalendarAdapter(getContext(), month, year, cells, events));
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        private int currentMonth;
        private int currentYear;

        private List<TranslatedArticle> events;

        CalendarAdapter(Context context, int currentMonth, int currentYear, List<Date> days, List<TranslatedArticle> events) {
            super(context, R.layout.view_calendar_date, days);

            this.currentMonth = currentMonth;
            this.currentYear = currentYear;

            this.events = events;
        }

        @NonNull
        @Override
        public View getView(int position, View view, @NonNull ViewGroup parent) {
            final Date date = formatDate(getItem(position));

            Date today = new Date();

            if (view == null) {
                view = CalendarDateView_.build(getContext());
            }

            CalendarDateView calendarDateView = ((CalendarDateView) view);

            if (isSingleSelectedDate(date)) {
                calendarDateView.setSingleSelectedDate();
            } else if (isIntervalLeftSelectedDate(date)) {
                calendarDateView.setIntervalLeftSelectedDate();
            } else if (isIntervalRightSelectedDate(date)) {
                calendarDateView.setIntervalRightSelectedDate();
            } else if (isIntervalCenterSelectedDate(date)) {
                calendarDateView.setIntervalCenterSelectedDate();
            } else if (isOtherMonthDate(date)) {
                calendarDateView.setOtherMonthDate();
            } else if (dateUtils.isSameDate(date, today)) {
                calendarDateView.setToday();
            } else if (isSpecialDate(date, events)) {
                calendarDateView.setArticleDate();
            } else {
                calendarDateView.setDefault();
            }

            ((CalendarDateView) view).setText("" + dateUtils.getDate(date));

            view.setOnClickListener(it -> adapter.onDateClicked(date));

            return view;
        }

        private Date formatDate(Date date) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTime();
        }

        private boolean isOtherMonthDate(Date date) {
            return currentMonth != dateUtils.getMonth(date) || currentYear != dateUtils.getYear(date);
        }

        private boolean isSpecialDate(Date date, List<TranslatedArticle> events) {
            Date formattedDate = formatDate(date);

            for (TranslatedArticle event : events) {
                Date formattedEventDate = formatDate(new Date(event.getReleaseDate()));

                if (formattedDate.equals(formattedEventDate)) {
                    return true;
                }
            }

            return false;
        }

        private boolean isSingleSelectedDate(Date date) {
            Date formattedDate = formatDate(date);

            return startDateSelected != null && endDateSelected == null && formattedDate.equals(startDateSelected);
        }

        private boolean isIntervalLeftSelectedDate(Date date) {
            Date formattedDate = formatDate(date);

            return startDateSelected != null && endDateSelected != null &&
                    formattedDate.equals(startDateSelected);
        }

        private boolean isIntervalRightSelectedDate(Date date) {
            Date formattedDate = formatDate(date);

            return startDateSelected != null && endDateSelected != null && formattedDate.equals(endDateSelected);
        }

        private boolean isIntervalCenterSelectedDate(Date date) {
            Date formattedDate = formatDate(date);

            return startDateSelected != null && endDateSelected != null &&
                    formattedDate.after(startDateSelected) &&
                    formattedDate.before(endDateSelected);
        }
    }
}
