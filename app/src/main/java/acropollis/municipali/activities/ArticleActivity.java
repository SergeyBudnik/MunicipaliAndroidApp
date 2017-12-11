package acropollis.municipali.activities;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.R;
import acropollis.municipali.view.article.ArticleContainerView;
import acropollis.municipali.view.rows.QuestionListRowView;
import acropollis.municipali.view.rows.QuestionListRowView_;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.answer.AnswerStatus;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.answer.UserAnswersService;
import acropollis.municipalidata.service.article.ArticleImageService;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_article)
public class ArticleActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int REDIRECT_FOR_VOTE = 1;

    @ViewById(R.id.article_container)
    ArticleContainerView articleContainerView;
    @ViewById(R.id.article_description)
    TextView articleDescriptionView;
    @ViewById(R.id.article_text)
    TextView articleTextView;
    @ViewById(R.id.questions_list)
    LinearLayout questionsListView;
    @ViewById(R.id.article_video)
    YouTubePlayerView articleVideoView;
    @ViewById(R.id.article_video_error)
    View articleVideoErrorView;

    @Extra("articleId")
    Long articleId;

    @Bean
    ArticleService articlesService;
    @Bean
    UserAnswersService userAnswerService;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @Bean
    ArticleImageService articleImageService;

    private TranslatedArticle article;

    @AfterViews
    void init() {
        article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        articleDescriptionView.setText(article.getDescription());
        articleTextView.setText(Html.fromHtml(article.getText()));
        articleContainerView.setArticle(article, true);

        questionsListView.removeAllViews();

        populateList();

        if (article.getVideo() != null) {
            articleVideoView.initialize(getResources().getString(R.string.youtube_api_key), this);
        } else {
            articleVideoView.setVisibility(View.GONE);
        }

        addArticleView();
    }

    @Background
    void addArticleView() {
        // ToDo: handle error

        RestResult<Void> result = articlesRestWrapper.addArticleView(
                productConfigurationService.getProductConfiguration(),
                articleId
        );

        if (result.isSuccessfull()) {
            System.out.println("OK");
        } else {
            System.out.println("FUCK");
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo(article.getVideo());
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        articleVideoView.setVisibility(View.GONE);
        articleVideoErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REDIRECT_FOR_VOTE) {
            questionsListView.removeAllViews();

            populateList();
        }
    }

    private void populateList() {
        Stream.of(article.getQuestions().values()).forEach(question -> {
            QuestionListRowView row = QuestionListRowView_.build(ArticleActivity.this);

            row.bind(articleId, question);
            row.setOnClickListener(it -> onQuestionClick(question));

            questionsListView.addView(row);
        });
    }

    private void onQuestionClick(TranslatedQuestion question) {
        ProductConfiguration configuration = productConfigurationService.getProductConfiguration();

        Class<? extends BaseActivity> questionActivity;

        boolean isAnswered = userAnswerService
                .getAnswerStatus(configuration, articleId, question.getId()) == AnswerStatus.ANSWERED;

        boolean isHidden = !question.isShowResult();

        switch (question.getAnswerType()) {
            case FIVE_POINTS:
                if (!isAnswered) {
                    questionActivity = QuestionFiveMarksVoteActivity_.class;
                } else if (!isHidden) {
                    questionActivity = QuestionFiveMarksVoteResultActivity_.class;
                } else {
                    questionActivity = QuestionFiveMarksVoteResultActivity_.class; // ToDo
                }
                break;
            case THREE_VARIANTS:
            case DYCHOTOMOUS:
                if (!isAnswered) {
                    questionActivity = QuestionMultipleVariantsActivity_.class;
                } else if (!isHidden) {
                    questionActivity = QuestionMultipleVariantsResultActivity_.class;
                } else {
                    questionActivity = QuestionMultipleVariantsResultActivity_.class; // ToDO
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
