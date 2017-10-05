package acropollis.municipali.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.view.question.ArticleCategoriesView;
import acropollis.municipalibootstrap.views.MunicipaliLoadableImageView;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleImageService;

@EViewGroup(R.layout.view_article_info)
public class ArticleInfoView extends RelativeLayout {
    @ViewById(R.id.icon)
    MunicipaliLoadableImageView iconView;
    @ViewById(R.id.title)
    TextView titleView;
    @ViewById(R.id.article_categories)
    ArticleCategoriesView articleCategoriesView;

    @Bean
    protected ProductConfigurationService productConfigurationService;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @Bean
    ArticleImageService articleImageService;

    public ArticleInfoView(Context context) {
        super(context);
    }

    public ArticleInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setArticle(TranslatedArticle article) {
        ProductConfiguration configuration = productConfigurationService.getProductConfiguration();

        iconView.configure(
                String.valueOf(article.getId()),
                R.color.light_gray,
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

        titleView.setText(article.getTitle());
        articleCategoriesView.bind(article);
    }
}
