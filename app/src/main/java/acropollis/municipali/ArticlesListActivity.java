package acropollis.municipali;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import acropollis.municipali.binders.MenuBinder;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.bootstrap.view.MunicipaliRefreshableListView;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.omega.ArticlesRestWrapper;
import acropollis.municipali.service.ArticlesService;

@EActivity(R.layout.activity_articles_list)
public class ArticlesListActivity extends BaseActivity {
    private static final int REDIRECT_FOR_QUESTION = 1;

    @ViewById(R.id.root)
    DrawerLayout rootView;

    @ViewById(R.id.articles_list)
    MunicipaliRefreshableListView articlesListView;

    @Bean
    MenuBinder menuBinder;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;

    @Bean
    ArticlesRestWrapper articlesRestWrapper;

    @Bean
    ArticlesService articlesService;

    @AfterViews
    void init() {
        menuBinder.bind(rootView);

        articlesListView.init(getArticlesLoader());
    }

    @Click(R.id.menu_button)
    void onMenuButtonClick() {
        rootView.openDrawer(Gravity.START);
    }

    @UiThread
    void showLoadingFailedMessage() {
        showMessage(getResources().getString(R.string.loading_failed));
    }

    @ItemClick(R.id.list)
    void onArticleRowClick(MunicipaliRowData articleRow) {
        redirectForResult(ArticleQuestionsListActivity_.class, 0, 0, REDIRECT_FOR_QUESTION,
                Collections.singletonMap("articleId", (Serializable) articleRow.getId()));
    }

    private MunicipaliRefreshableListView.RowsLoader getArticlesLoader() {
        return new MunicipaliRefreshableListView.RowsLoader() {
            @Override
            public void load(final MunicipaliRefreshableListView.LoadingListener listener) {
                listener.onReadingFromCache(articleBootstrapAdapter.getArticlesRows(
                        new ArrayList<>(articlesService.getArticles())
                ));

                articlesRestWrapper.loadArticles(new RestListener<Void>() {
                    @Override
                    public void onSuccess(Void o) {
                        listener.onSuccess(articleBootstrapAdapter.getArticlesRows(
                                new ArrayList<>(articlesService.getArticles())
                        ));
                    }

                    @Override
                    public void onFailure() {
                        listener.onFailure();

                        showLoadingFailedMessage();
                    }
                });
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REDIRECT_FOR_QUESTION) {
            articlesListView.reinit();
        }
    }
}
