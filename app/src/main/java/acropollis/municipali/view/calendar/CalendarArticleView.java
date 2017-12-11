package acropollis.municipali.view.calendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import acropollis.municipali.activities.ArticleActivity_;
import acropollis.municipali.activities.BaseActivity;
import acropollis.municipali.R;
import acropollis.municipali.utls.DateUtils;
import acropollis.municipalidata.dto.article.TranslatedArticle;

@EViewGroup(R.layout.view_calendar_article)
public class CalendarArticleView extends LinearLayout {
    @ViewById(R.id.top_background)
    View topBackgroundView;
    @ViewById(R.id.bottom_background)
    View bottomBackgroundView;
    @ViewById(R.id.date)
    TextView dateView;
    @ViewById(R.id.month)
    TextView monthView;
    @ViewById(R.id.text)
    TextView textView;
    @ViewById(R.id.separator)
    View separatorView;

    @Bean
    DateUtils dateUtils;

    public CalendarArticleView(Context context) {
        super(context);
    }

    public CalendarArticleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(TranslatedArticle article, boolean first, boolean last, boolean dateChanged) {
        textView.setText(article.getTitle());

        Date releaseDate = new Date(article.getReleaseDate());

        dateView.setText(dateChanged ? String.format(Locale.ENGLISH, "%d", dateUtils.getDate(releaseDate)) : "");
        monthView.setText(dateChanged ? dateUtils.getShortMonthText(dateUtils.getMonth(releaseDate)) : "");

        separatorView.setVisibility(dateChanged && !first ? VISIBLE : GONE);

        topBackgroundView.setVisibility(first ? GONE : VISIBLE);
        bottomBackgroundView.setVisibility(last ? GONE : VISIBLE);

        setOnClickListener(it -> ((BaseActivity) getContext()).
                redirectForResult(ArticleActivity_.class, 0, 0, 1,
                        Collections.singletonMap("articleId", article.getId()))
        );
    }
}
