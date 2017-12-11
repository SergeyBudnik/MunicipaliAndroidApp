package acropollis.municipali.view.calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.ComparatorCompat;
import com.annimon.stream.Objects;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import acropollis.municipali.R;
import acropollis.municipali.comparators.ArticlesComparator;
import acropollis.municipali.predicates.ArticlePredicates;
import acropollis.municipali.utls.DateUtils;
import acropollis.municipalidata.dto.article.TranslatedArticle;

@EViewGroup(R.layout.view_calendar)
public class CalendarView extends LinearLayout implements CalendarSelectionIntervalListener {
    @ViewById(R.id.month_and_year)
    TextView monthAndYearView;

    @ViewById(R.id.previous_month)
    ImageView previousMonthView;
    @ViewById(R.id.next_month)
    ImageView nextMonthView;

    @ViewById(R.id.calendar_pager)
    ViewPager viewPager;

    @ViewById(R.id.calendar_articles_list)
    LinearLayout calendarArticlesListView;

    @Bean
    DateUtils dateUtils;

    @Bean
    CalendarViewPagerAdapter calendarViewPagerAdapter;

    @Bean
    ArticlePredicates articlePredicates;

    private Date startDate, endDate;

    private Collection<TranslatedArticle> articles;
    private CalendarSelectionIntervalListener calendarSelectionIntervalListener;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init() {
        setArticles(new ArrayList<>());
    }

    public void setCalendarSelectionIntervalListener(CalendarSelectionIntervalListener calendarSelectionIntervalListener) {
        this.calendarSelectionIntervalListener = calendarSelectionIntervalListener;
    }

    public void setArticles(Collection<TranslatedArticle> articles) {
        this.articles = articles;

        final Date today = new Date();

        calendarViewPagerAdapter.init(this, Stream.of(articles).toList(), startDate, endDate);

        setMonthAndYearText(today, dateUtils.getMonth(today), dateUtils.getYear(today));

        viewPager.setAdapter(calendarViewPagerAdapter);
        viewPager.setCurrentItem(CalendarViewPagerAdapter.MONTHS_AMOUNT / 2);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                CalendarMonthView calendarMonthFragment =
                        (CalendarMonthView) calendarViewPagerAdapter.getItem(position);

                setMonthAndYearText(today, calendarMonthFragment.getMonth(), calendarMonthFragment.getYear());

                previousMonthView.setImageDrawable(getResources().getDrawable(
                        position == 0 ?
                                R.drawable.calendar_arrow_left_inactive :
                                R.drawable.calendar_arrow_left));

                nextMonthView.setImageDrawable(getResources().getDrawable(
                        position == CalendarViewPagerAdapter.MONTHS_AMOUNT - 1 ?
                                R.drawable.calendar_arrow_right_inactive :
                                R.drawable.calendar_arrow_right));
            }
        });

        fillArticlesList(startDate, endDate);
    }

    @Click(R.id.month_and_year)
    void onMonthAndYearClick() {
        viewPager.setCurrentItem(CalendarViewPagerAdapter.MONTHS_AMOUNT / 2);
    }

    @Click(R.id.previous_month)
    void onPrevMonthClick() {
        int current = viewPager.getCurrentItem();

        viewPager.setCurrentItem(current > 0 ? current - 1 : 0);
    }

    @Click(R.id.next_month)
    void onNextMonthClick() {
        int current = viewPager.getCurrentItem();

        viewPager.setCurrentItem(current < CalendarViewPagerAdapter.MONTHS_AMOUNT - 1 ?
                current + 1 :
                CalendarViewPagerAdapter.MONTHS_AMOUNT - 1);
    }

    @Override
    public void onCalendarSelectionIntervalChanged(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        fillArticlesList(startDate, endDate);

        if (calendarSelectionIntervalListener != null) {
            calendarSelectionIntervalListener.onCalendarSelectionIntervalChanged(startDate, endDate);
        }
    }

    private void setMonthAndYearText(Date today, int month, int year) {
        monthAndYearView.setText(getResources().getString(
                R.string.calendar_date,
                dateUtils.getMonthText(month).toUpperCase(),
                year)
        );

        monthAndYearView.setTextColor(
                getResources().getColor(
                        month == dateUtils.getMonth(today) && year == dateUtils.getYear(today) ?
                                R.color.black :
                                R.color.gray_2
                )
        );
    }

    private void fillArticlesList(Date startDate, Date endDate) {
        calendarArticlesListView.removeAllViews();

        for (View v : getArticlesListItems(startDate, endDate)) {
            calendarArticlesListView.addView(v);
        }
    }

    private List<View> getArticlesListItems(Date startDate, Date endDate) {
        List<TranslatedArticle> sortedArticles = Stream.of(articles)
                .sorted(ComparatorCompat.reversed(new ArticlesComparator()))
                .filter(articlePredicates.articleReleaseDateMatch(startDate, endDate))
                .toList();

        List<View> sortedArticlesViews = new ArrayList<>();

        int articleIndex = 0;

        for (TranslatedArticle article : sortedArticles) {
            boolean first = articleIndex == 0;
            boolean last = articleIndex == sortedArticles.size() - 1;

            boolean dateChanged = articleIndex == 0 ||
                    !Objects.equals(
                            dateUtils.formatDate(new Date(article.getReleaseDate())),
                            dateUtils.formatDate(new Date(sortedArticles.get(articleIndex - 1).getReleaseDate()))
                    );

            CalendarArticleView calendarArticleView = CalendarArticleView_.build(getContext()); {
                calendarArticleView.init(
                        article,
                        first,
                        last,
                        dateChanged
                );
            }

            sortedArticlesViews.add(calendarArticleView);

            articleIndex++;
        }

        return sortedArticlesViews;
    }
}
