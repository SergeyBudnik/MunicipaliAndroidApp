package acropollis.municipali.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.widget.ListView;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import acropollis.municipali.R;
import acropollis.municipali.adapter.ArticlesListAdapter;
import acropollis.municipali.predicates.ArticlePredicates;
import acropollis.municipali.view.HeaderView;
import acropollis.municipali.view.calendar.CalendarSelectionIntervalListener;
import acropollis.municipali.view.calendar.CalendarView;
import acropollis.municipalibootstrap.views.MunicipaliPopupMessageView.Type;
import acropollis.municipalidata.dto.article.Article;
import acropollis.municipalidata.dto.article.ArticleType;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleService;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@EActivity(R.layout.activity_articles_list)
public class ArticlesListActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, CalendarSelectionIntervalListener {

    public static final String ARTICLES_TYPE_EXTRA = "articlesType";

    private static final int REDIRECT_FOR_QUESTION = 1;

    @ViewById(R.id.root)
    DrawerLayout rootView;

    @ViewById(R.id.header)
    HeaderView headerView;
    @ViewById(R.id.articles_list_refresh)
    SwipeRefreshLayout articlesListRefreshView;
    @ViewById(R.id.articles_list)
    ListView articlesListView;

    @ViewById(R.id.calendar)
    CalendarView calendarView;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @Bean
    ArticlesListAdapter articlesListAdapter;

    @Bean
    ArticleService articlesService;

    @Bean
    ArticlePredicates articlePredicates;

    @Extra(ARTICLES_TYPE_EXTRA)
    ArticleType articlesType;

    private Date startDate, endDate;

    @AfterViews
    void init() {
        headerView.setStyle(HeaderView.Style.DEFAULT);
        headerView.setTitle(articlesType == ArticleType.NEWS ? R.string.city_news : R.string.city_events);
        headerView.setLeftButton(R.drawable.menu_button, it -> rootView.openDrawer(Gravity.START));
        headerView.setRightButton(R.drawable.calendar_button, it ->
            calendarView.setVisibility(calendarView.getVisibility() == GONE ? VISIBLE : GONE)
        );

        articlesListRefreshView.setOnRefreshListener(this);

        articlesListView.setAdapter(articlesListAdapter);
        calendarView.setCalendarSelectionIntervalListener(this);

        setArticles(startDate, endDate);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        articlesListRefreshView.setRefreshing(true);

        loadArticles();
    }

    @Background
    void loadArticles() {
        RestResult<List<Article>> articles = articlesRestWrapper
                .loadArticles(productConfigurationService.getProductConfiguration());

        if (articles.isSuccessfull()) {
            onArticlesLoadSuccessful();
        } else {
            onArticlesLoadFailed();
        }
    }

    @UiThread
    void onArticlesLoadSuccessful() {
        articlesListRefreshView.setRefreshing(false);

        setArticles(startDate, endDate);
    }

    @UiThread
    void onArticlesLoadFailed() {
        showMessage(getResources().getString(R.string.loading_failed), Type.ERROR);

        articlesListRefreshView.setRefreshing(false);
    }

    @UiThread
    void setArticles(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        Collection<TranslatedArticle> allArticles = articlesService.getArticles(getCurrentProductConfiguration());

        Collection<TranslatedArticle> filteredArticles = Stream
                .of(allArticles)
                .filter(articlePredicates.articleReleaseDateMatch(startDate, endDate))
                .toList();

        articlesListAdapter.setElements(articlesType, filteredArticles);
        articlesListAdapter.notifyDataSetChanged();

        calendarView.setArticles(Stream.of(allArticles).filter(it -> it.getType() == articlesType).toList());
    }

    @ItemClick(R.id.articles_list)
    void onArticleRowClick(TranslatedArticle article) {
        redirectForResult(ArticleActivity_.class, 0, 0, REDIRECT_FOR_QUESTION,
                Collections.singletonMap("articleId", article.getId()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REDIRECT_FOR_QUESTION) {
            articlesListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCalendarSelectionIntervalChanged(Date startDate, Date endDate) {
        setArticles(startDate, endDate);
    }
}
