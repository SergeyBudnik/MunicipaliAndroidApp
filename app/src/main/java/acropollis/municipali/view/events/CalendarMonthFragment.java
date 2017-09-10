package acropollis.municipali.view.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import acropollis.municipali.R;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipali.utls.DateUtils;

@EFragment(R.layout.fragment_calendar_month)
public class CalendarMonthFragment extends Fragment {
    private static final int DAYS_IN_CALENDAR = 35;

    @ViewById(R.id.calendar_grid)
    GridView calendarGridView;

    @Bean
    DateUtils dateUtils;

    private Calendar currentDate = Calendar.getInstance();

    private int month;
    private int year;

    private List<TranslatedArticle> events = new ArrayList<>();
    private EventCalendarFragment.SpecialDateOnClickListener specialDateClickListener = null;

    @AfterViews
    void updateCalendar() {
        updateCalendar(month, year, events);
    }

    public void setEvents(
            int month,
            int year,
            List<TranslatedArticle> events,
            EventCalendarFragment.SpecialDateOnClickListener specialClickDateListener) {

        this.month = month;
        this.year = year;

        this.events = events;
        this.specialDateClickListener = specialClickDateListener;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void updateCalendar(int month, int year, List<TranslatedArticle> events) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        while (cells.size() < DAYS_IN_CALENDAR)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendarGridView.setAdapter(new CalendarAdapter(getActivity(), month, year, cells, events));
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        private int currentMonth;
        private int currentYear;

        private List<TranslatedArticle> events;

        CalendarAdapter(Context context, int currentMonth, int currentYear, List<Date> days, List<TranslatedArticle> events) {
            super(context, R.layout.view_event_calendar_current_month_date, days);

            this.currentMonth = currentMonth;
            this.currentYear = currentYear;

            this.events = events;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final Date date = formatDate(getItem(position));

            Date today = new Date();

            if (view == null) {
                if (isOtherMonthDate(date)) {
                    view = EventCalendarOtherMonthDateView_.build(getContext());
                } else if (dateUtils.isSameDate(date, today)) {
                    view = EventCalendarCurrentDateView_.build(getContext());
                } else if (isSpecialDate(date, events)) {
                    view = EventCalendarSpecialDateView_.build(getContext());

                    if (specialDateClickListener != null) {
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                specialDateClickListener.onSpecialDateClick(date);
                            }
                        });
                    }
                } else {
                    view = EventCalendarCurrentMonthDateView_.build(getContext());
                }
            }

            ((EventCalendarDateView) view).setText("" + dateUtils.getDate(date));

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
                Date formattedEventDate = formatDate(new Date(event.getExpirationDate()));

                if (formattedDate.equals(formattedEventDate)) {
                    return true;
                }
            }

            return false;
        }
    }
}
