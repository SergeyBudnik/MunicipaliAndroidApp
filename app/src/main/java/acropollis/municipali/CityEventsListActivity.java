package acropollis.municipali;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import acropollis.municipali.binders.MenuBinder;
import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap.view.MunicipaliRowView_;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipali.data.article.TranslatedArticle;
import acropollis.municipali.service.EventsService;
import acropollis.municipali.utls.DateUtils;
import acropollis.municipali.utls.ScreenUtils;
import acropollis.municipali.view.events.EventCalendarFragment;

@EActivity(R.layout.activity_city_events_list)
public class CityEventsListActivity extends BaseActivity
        implements EventCalendarFragment.SpecialDateOnClickListener {

    private static final int REDIRECT_FOR_QUESTION = 1;

    @ViewById(R.id.root)
    DrawerLayout rootView;

    @FragmentById(R.id.calendar)
    EventCalendarFragment calendarView;

    @ViewById(R.id.current_day_of_week)
    TextView currentDayOfWeekView;
    @ViewById(R.id.current_date)
    TextView currentDateView;

    @ViewById(R.id.today_events)
    LinearLayout todayEventsView;
    @ViewById(R.id.soon_events)
    LinearLayout soonEventsView;

    @ViewById(R.id.no_today_events)
    View noTodayEventsView;
    @ViewById(R.id.no_soon_events)
    View noSoonEventsView;

    @ViewById(R.id.events_scroll)
    View eventsScrollView;

    @Bean
    EventsService eventsService;

    @Bean
    ScreenUtils screenUtils;
    @Bean
    DateUtils dateUtils;
    
    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;

    @Bean
    MenuBinder menuBinder;

    private List<MunicipaliRowView> soonEventsViews = new ArrayList<>();

    @AfterViews
    void init() {
        menuBinder.bind(rootView);

        calendarView.setEvents(eventsService.getThisMonthEvents(), this);

        Date currentDate = new Date();

        currentDayOfWeekView.setText(dateUtils.getDayOfWeekText(dateUtils.getDayOfWeek(currentDate)));

        currentDateView.setText(getString(R.string.date,
                dateUtils.getMonthText(dateUtils.getMonth(currentDate)),
                dateUtils.getDate(currentDate),
                dateUtils.getYear(currentDate)));

        todayEventsView.removeAllViews();

        List<MunicipaliRowView> todayEventsViews = buildTodayEventListItems();

        if (todayEventsViews.size() != 0) {
            noTodayEventsView.setVisibility(View.GONE);

            for (View v : todayEventsViews) {
                todayEventsView.addView(v);
            }
        } else {
            noTodayEventsView.setVisibility(View.VISIBLE);
        }

        soonEventsView.removeAllViews();

        soonEventsViews = buildSoonEventListItems();

        if (soonEventsViews.size() != 0) {
            noSoonEventsView.setVisibility(View.GONE);

            for (View v : soonEventsViews) {
                soonEventsView.addView(v);
            }
        } else {
            noSoonEventsView.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.menu_button)
    void onMenuButtonClick() {
        rootView.openDrawer(Gravity.START);
    }

    @UiThread
    @Override
    public void onSpecialDateClick(Date date) {
//        for (MunicipaliRowView v : soonEventsViews) {
//            if (dateUtils.isSameDate(new Date(v.getDate()), date)) {
//                eventsScrollView.scrollTo(0, ((View) v.getParent()).getTop() + v.getTop());
//                break;
//            }
//        }
//
//        for (MunicipaliRowView v : soonEventsViews) {
//            if (dateUtils.isSameDate(new Date(v.getDate()), date)) {
//                v.animateScrollTo();
//            }
//        }
    }

    private List<MunicipaliRowView> buildTodayEventListItems() {
        List<MunicipaliRowView> todayEventsListItems = new ArrayList<>();

        for (final TranslatedArticle event : eventsService.getTodayEvents()) {
            MunicipaliRowView todayEventsListItemView = MunicipaliRowView_.build(this);

            todayEventsListItemView.bind(articleBootstrapAdapter.getArticleRowInfo(event, true));
            todayEventsListItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectForResult(ArticleQuestionsListActivity_.class, 0, 0, REDIRECT_FOR_QUESTION,
                            Collections.singletonMap("articleId", (Serializable) event.getId()));
                }
            });

            todayEventsListItems.add(todayEventsListItemView);
        }

        return todayEventsListItems;
    }

    private List<MunicipaliRowView> buildSoonEventListItems() {
        List<MunicipaliRowView> soonEventsListItems = new ArrayList<>();

        for (final TranslatedArticle event : eventsService.getSoonEvents()) {
            MunicipaliRowView soonEventsListItemView = MunicipaliRowView_.build(this);

            soonEventsListItemView.bind(articleBootstrapAdapter.getArticleRowInfo(event, true));
            soonEventsListItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    redirectForResult(ArticleQuestionsListActivity_.class, 0, 0, REDIRECT_FOR_QUESTION,
                            Collections.singletonMap("articleId", (Serializable) event.getId()));
                }
            });

            soonEventsListItems.add(soonEventsListItemView);
        }

        return soonEventsListItems;
    }
}
