package acropollis.municipali.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.Locale;

import acropollis.municipali.R;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.utls.DateUtils;
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
    @ViewById(R.id.date)
    TextView dateView;
    @ViewById(R.id.questions_amount)
    TextView questionsAmountView;
    @ViewById(R.id.image)
    MunicipaliLoadableImageView imageView;

    @Bean
    ArticleImageService articleImageService;
    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @Bean
    ProductConfigurationService productConfigurationService;

    @Bean
    DateUtils dateUtils;

    public ArticlesListRowView(Context context) {
        super(context);
    }

    public ArticlesListRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(TranslatedArticle article) {
        textView.setText(article.getTitle());
        dateView.setText(getDateText(article));

        if (getQuestionsAmount(article) == 0) {
            questionsAmountView.setVisibility(GONE);
        } else {
            questionsAmountView.setVisibility(VISIBLE);
            questionsAmountView.setText(getQuestionsAmountText(article));
        }

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

    private String getDateText(TranslatedArticle article) {
        Date releaseDate = new Date(article.getReleaseDate());

        int date = dateUtils.getDate(releaseDate);
        String month = dateUtils.getMonthText(dateUtils.getMonth(releaseDate));
        int year = dateUtils.getYear(releaseDate);

        return String.format(Locale.ENGLISH, "%s %d, %d", month, date, year);
    }

    private String getQuestionsAmountText(TranslatedArticle article) {
        int questionsAmount = getQuestionsAmount(article);

        if (questionsAmount < 100) {
            return String.format(Locale.ENGLISH, "%d", questionsAmount);
        } else {
            return "99+";
        }
    }

    // ToDo: count answered
    private int getQuestionsAmount(TranslatedArticle article) {
        return article.getQuestions().size();
    }
}
