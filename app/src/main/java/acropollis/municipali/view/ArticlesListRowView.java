package acropollis.municipali.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipalibootstrap.views.MunicipaliLoadableImageView;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleImageService;

@EViewGroup(R.layout.view_article_list_row)
public class ArticlesListRowView extends LinearLayout {
    @ViewById(R.id.text)
    TextView textView;
    @ViewById(R.id.image)
    MunicipaliLoadableImageView imageView;

    @Bean
    ArticleImageService articleImageService;
    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @Bean
    ProductConfigurationService productConfigurationService;

    public ArticlesListRowView(Context context) {
        super(context);
    }

    public ArticlesListRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(TranslatedArticle article) {
        textView.setText(article.getTitle());

        ProductConfiguration configuration = productConfigurationService.getProductConfiguration();

        imageView.configure(
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
    }
}
