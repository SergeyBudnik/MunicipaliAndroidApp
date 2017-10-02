package acropollis.municipali;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.binders.MenuBinder;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipali.bootstrap_adapter.QuestionBootstrapAdapter;
import acropollis.municipali.data.AnswerStatus;
import acropollis.municipali.service.UserAnswerService;
import acropollis.municipali.view.QuestionListRowView;
import acropollis.municipali.view.QuestionListRowView_;
import acropollis.municipali.view.question.ArticleCategoriesView;
import acropollis.municipalibootstrap.views.MunicipaliLoadableImageView;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleImageService;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_article)
public class ArticleActivity extends BaseActivity {
    private static final int REDIRECT_FOR_VOTE = 1;

    @ViewById(R.id.root)
    DrawerLayout rootView;

    @ViewById(R.id.icon)
    MunicipaliLoadableImageView iconView;
    @ViewById(R.id.title)
    TextView titleView;
    @ViewById(R.id.article_categories)
    ArticleCategoriesView articleCategoriesView;
    @ViewById(R.id.article_text)
    TextView articleTextView;
    @ViewById(R.id.questions_list)
    LinearLayout questionsListView;

    @ViewById(R.id.article_video)
    WebView aricleVideoView;

    @Extra("articleId")
    Long articleId;

    @Bean
    ArticleService articlesService;
    @Bean
    UserAnswerService userAnswerService;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @Bean
    ArticleImageService articleImageService;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;
    @Bean
    QuestionBootstrapAdapter questionBootstrapAdapter;

    @Bean
    MenuBinder menuBinder;

    private TranslatedArticle article;

    @AfterViews
    void init() {
        article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

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
        articleTextView.setText(article.getText());


        questionsListView.removeAllViews();

        Stream.of(article.getQuestions().values()).forEach(question -> {
            QuestionListRowView row = QuestionListRowView_.build(ArticleActivity.this);

            row.bind(question);
            row.setOnClickListener(it -> onQuestionClick(question));

            questionsListView.addView(row);
        });

        if (article.getVideo() != null) {
            aricleVideoView.setVisibility(View.VISIBLE);
            aricleVideoView.getSettings().setJavaScriptEnabled(true);
            aricleVideoView.getSettings().setPluginState(WebSettings.PluginState.ON);
            aricleVideoView.loadUrl(article.getVideo());
            aricleVideoView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }

    }

    @Click(R.id.back_button)
    void onBackClick() {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    @Click(R.id.history)
    void onHistoryClick() {
        Map<String, Serializable> extras = new HashMap<>(); {
            extras.put("articleId", articleId);
        }

        redirect(ArticleHistoryActivity_.class, 0, 0, false, extras);
    }

    @Override
    public void onBackPressed() {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REDIRECT_FOR_VOTE) {
            //articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, true));
//            questionsListView.setElements(
//                    questionBootstrapAdapter.getQuestionsRows(article),
//                    new MunicipaliLayoutListView.RowClickListener() {
//                        @Override
//                        public void onClick(int position, MunicipaliRowData rowData) {
//                            onQuestionClick(rowData);
//                        }
//                    }
//            );
        }
    }

    private void onQuestionClick(TranslatedQuestion question) {
        Class<? extends BaseActivity> questionActivity;

        boolean isAnswered = userAnswerService.getAnswerStatus(articleId, question.getId()) == AnswerStatus.ANSWERED;
        boolean isHidden = !question.isShowResult();

        switch (question.getAnswerType()) {
            case FIVE_POINTS:
                if (!isAnswered) {
                    questionActivity = FiveMarksVoteActivity_.class;
                } else if (!isHidden) {
                    questionActivity = FiveMarksVoteResultActivity_.class;
                } else {
                    questionActivity = VoteHiddenResultActivity_.class;
                }
                break;
            case THREE_VARIANTS:
            case DYCHOTOMOUS:
                if (!isAnswered) {
                    questionActivity = QuestionMultipleVariantsActivity_.class;
                } else if (!isHidden) {
                    questionActivity = MultipleVariantsVoteResultActivity_.class;
                } else {
                    questionActivity = VoteHiddenResultActivity_.class;
                }
                break;
            default:
                questionActivity = null;
        }

        Map<String, Serializable> extras = new HashMap<>();

        extras.put("articleId", articleId);
        extras.put("questionId", question.getId());

        redirectForResult(questionActivity, 0, 0, REDIRECT_FOR_VOTE, extras);
    }
}
