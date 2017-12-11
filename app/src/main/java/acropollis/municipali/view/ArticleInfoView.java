package acropollis.municipali.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

import acropollis.municipali.R;
import acropollis.municipali.utls.DateUtils;
import acropollis.municipali.view.article.ArticleCategoriesView;
import acropollis.municipalibootstrap.views.MunicipaliProportionsView;
import acropollis.municipalidata.dto.article.ArticleType;
import acropollis.municipalidata.dto.article.TranslatedArticle;

@EViewGroup(R.layout.view_article_info)
public class ArticleInfoView extends RelativeLayout {
    @ViewById(R.id.icon_top)
    MunicipaliProportionsView iconTopView;
    @ViewById(R.id.type)
    TextView typeView;
    @ViewById(R.id.date)
    TextView dateView;
    @ViewById(R.id.title)
    TextView titleView;
    @ViewById(R.id.article_categories)
    ArticleCategoriesView articleCategoriesView;

    @Bean
    DateUtils dateUtils;

    public ArticleInfoView(Context context) {
        super(context);
    }

    public ArticleInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setArticle(TranslatedArticle article, boolean fullSizeImage) {
        iconTopView.setWidthRario(fullSizeImage ? 1 : 3);
        iconTopView.setHeightRatio(fullSizeImage ? 1 : 2);

        typeView.setText(article.getType() == ArticleType.EVENT ?
                R.string.article_type_event :
                R.string.article_type_news
        );

        titleView.setText(article.getTitle());
        dateView.setText(dateUtils.getDateText(new Date(article.getCalendarStartDate())));
        articleCategoriesView.bind(article);
    }

    public void setScrollDistance(double distance) {
        iconTopView.setAlpha(distance > 1 ? 1.0f : (float) distance);
    }
}
