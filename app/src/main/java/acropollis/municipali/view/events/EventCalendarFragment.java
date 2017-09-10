package acropollis.municipali.view.events;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import acropollis.municipali.R;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipali.utls.DateUtils;

@EFragment(R.layout.fragment_calendar)
public class EventCalendarFragment extends Fragment {
    public interface SpecialDateOnClickListener {
        void onSpecialDateClick(Date date);
    }

    @ViewById(R.id.month_and_year)
    TextView monthAndYearView;

    @ViewById(R.id.previous_month)
    ImageView previousMonthView;
    @ViewById(R.id.next_month)
    ImageView nextMonthView;

    @ViewById(R.id.calendar_pager)
    ViewPager viewPager;

    @Bean
    DateUtils dateUtils;

    @AfterViews
    void init() {
        setEvents(new ArrayList<TranslatedArticle>(), null);
    }

    public void setEvents(List<TranslatedArticle> events, SpecialDateOnClickListener specialClickDateListener) {
        final Date today = new Date();

        final EventCalendarViewPagerAdapter eventCalendarViewPagerAdapter = new EventCalendarViewPagerAdapter(
                getActivity().getSupportFragmentManager(),
                today,
                dateUtils,
                events,
                specialClickDateListener
        );

        setMonthAndYearText(today, dateUtils.getMonth(today), dateUtils.getYear(today));

        viewPager.setAdapter(eventCalendarViewPagerAdapter);
        viewPager.setCurrentItem(EventCalendarViewPagerAdapter.MONTHS_AMOUNT / 2);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                CalendarMonthFragment calendarMonthFragment =
                        (CalendarMonthFragment) eventCalendarViewPagerAdapter.getItem(position);

                setMonthAndYearText(today, calendarMonthFragment.getMonth(), calendarMonthFragment.getYear());

                previousMonthView.setImageDrawable(getResources().getDrawable(
                        position == 0 ?
                                R.drawable.calendar_arrow_left_inactive :
                                R.drawable.calendar_arrow_left));

                nextMonthView.setImageDrawable(getResources().getDrawable(
                        position == EventCalendarViewPagerAdapter.MONTHS_AMOUNT - 1 ?
                                R.drawable.calendar_arrow_right_inactive :
                                R.drawable.calendar_arrow_right));
            }
        });
    }

    @Click(R.id.month_and_year)
    void onMonthAndYearClick() {
        viewPager.setCurrentItem(EventCalendarViewPagerAdapter.MONTHS_AMOUNT / 2);
    }

    @Click(R.id.previous_month)
    void onPrevMonthClick() {
        int current = viewPager.getCurrentItem();

        viewPager.setCurrentItem(current > 0 ?
                current - 1 :
                0);
    }

    @Click(R.id.next_month)
    void onNextMonthClick() {
        int current = viewPager.getCurrentItem();

        viewPager.setCurrentItem(current < EventCalendarViewPagerAdapter.MONTHS_AMOUNT - 1 ?
                current + 1 :
                EventCalendarViewPagerAdapter.MONTHS_AMOUNT - 1);
    }

    private void setMonthAndYearText(Date today, int month, int year) {
        monthAndYearView.setText(getResources().getString(
                R.string.calendar_date,
                dateUtils.getMonthText(month),
                year));

        monthAndYearView.setTextColor(
                getResources().getColor(
                        month == dateUtils.getMonth(today) && year == dateUtils.getYear(today) ?
                                R.color.primary_black :
                                R.color.primary_gray
                )
        );
    }
}
