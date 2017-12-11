package acropollis.municipali.view.article;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.activities.BaseActivity;
import acropollis.municipali.R;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.view.ArticleInfoView;
import acropollis.municipali.view.HeaderView;
import acropollis.municipali.view.MenuView;
import acropollis.municipalibootstrap.views.MunicipaliLoadableImageView;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleImageService;

import static android.app.Activity.RESULT_OK;

@EViewGroup(R.layout.view_article_container)
public class ArticleContainerView extends RelativeLayout {
    @ViewById(R.id.root)
    DrawerLayout rootView;
    @ViewById(R.id.menu)
    MenuView menuView;

    @ViewById(R.id.header)
    HeaderView headerView;
    @ViewById(R.id.icon)
    MunicipaliLoadableImageView iconView;
    @ViewById(R.id.container_scroll)
    ScrollView containerScrollView;
    @ViewById(R.id.article_info)
    ArticleInfoView articleInfoView;

    @ViewById(R.id.container)
    LinearLayout containerView;

    @Bean
    protected ProductConfigurationService productConfigurationService;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @Bean
    ArticleImageService articleImageService;

    private Map<View, ViewGroup.LayoutParams> customViews = new HashMap<>();

    public ArticleContainerView(Context context) {
        super(context);
    }

    public ArticleContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    public void init() {
        Stream.of(customViews).forEach(it -> containerView.addView(it.getKey(), it.getValue()));

        containerScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int x = containerScrollView.getWidth();
            int y = containerScrollView.getScrollY();

            articleInfoView.setScrollDistance(2.0 * y / x);
        });
    }

    public void setArticle(TranslatedArticle article, boolean fullSizeImage) {
        ProductConfiguration configuration = productConfigurationService.getProductConfiguration();

        iconView.setWidthRario(fullSizeImage ? 1 : 3);
        iconView.setHeightRatio(fullSizeImage ? 1 : 2);

        iconView.configure(
                String.valueOf(article.getId()),
                R.color.gray_3,
                () -> articleImageService.getArticleImage(configuration, article.getId()).orElse(null),
                () -> {
                    RestResult<byte []> image = articlesRestWrapper.loadArticleImage(configuration, article.getId());

                    if (image.isSuccessfull()) {
                        return image.getData() != null ? image.getData() : new byte [0];
                    } else {
                        return null;
                    }
                }
        );

        headerView.setStyle(HeaderView.Style.TRANSPARENT);
        headerView.setTitle(R.string.article);
        headerView.setLeftButton(
                R.drawable.back_button,
                it -> ((BaseActivity) getContext()).finishRedirectForResult(0, 0, RESULT_OK)
        );

        articleInfoView.setArticle(article, fullSizeImage);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isCustomView(child.getId())) {
            customViews.put(child, params);
        } else {
            super.addView(child, params);
        }
    }

    private boolean isCustomView(int id) {
        return id != R.id.container_root;
    }
}
