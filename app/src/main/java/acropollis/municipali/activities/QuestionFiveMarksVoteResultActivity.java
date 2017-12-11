package acropollis.municipali.activities;

import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.Map;

import acropollis.municipali.R;
import acropollis.municipali.data.FiveMarksVoteResult;
import acropollis.municipali.view.article.ArticleContainerView;
import acropollis.municipali.view.common.SegmentedView;
import acropollis.municipalibootstrap.views.MunicipaliPopupMessageView.Type;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.rest_wrapper.answer.AnswerRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.answer.UserAnswersService;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_five_marks_vote_result)
public class QuestionFiveMarksVoteResultActivity extends BaseActivity {
    @ViewById(R.id.article_container)
    ArticleContainerView articleContainerView;

    @ViewById(R.id.question_text)
    TextView questionTextView;

    @ViewById(R.id.your_vote_segmented_oval)
    SegmentedView yourVoteSegmentedOvalView;
    @ViewById(R.id.your_vote_value)
    TextView yourVoteValueView;

    @ViewById(R.id.average_vote_segmented_oval)
    SegmentedView averageVoteSegmentedOvalView;
    @ViewById(R.id.average_vote_value)
    TextView averageVoteValueView;
    @ViewById(R.id.average_vote_loading_spinner)
    View averageVoteLoadingSpinner;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnimation;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    long questionId;

    @Bean
    AnswerRestWrapper answerRestWrapper;

    @Bean
    ArticleService articlesService;
    @Bean
    UserAnswersService userAnswersService;

    @Bean
    ArticleRestWrapper articleRestWrapper;

    @AfterViews
    void init() {
        ProductConfiguration configuration = productConfigurationService.getProductConfiguration();

        TranslatedArticle article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        articleContainerView.setArticle(article, false);

        TranslatedQuestion question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());

        final FiveMarksVoteResult voteResult = getVoteResult(question, userAnswersService
                .getAnswer(configuration, articleId, questionId).orElseThrow(RuntimeException::new));

        yourVoteSegmentedOvalView.setPercents(20 * voteResult.getSegmentsAmount());
        yourVoteValueView.setText(getResources().getString(voteResult.getVoteValueTextId()));

        averageVoteLoadingSpinner.startAnimation(spinnerAnimation);

        getAnswer(article.getId(), question.getId(), voteResult);
    }

    @Background
    void getAnswer(long articleId, long questionId, FiveMarksVoteResult voteResult) {
        RestResult<Map<Long, Long>> result = answerRestWrapper.getAnswer(
                productConfigurationService.getProductConfiguration(),
                articleId,
                questionId
        );

        if (result.isSuccessfull()) {
            onGetStatisticsSuccessful(result.getData(), voteResult);
        } else {
            onGetStatisticsFail();
        }
    }

    @UiThread
    void onGetStatisticsSuccessful(Map<Long, Long> answerStatistics, FiveMarksVoteResult voteResult) {
        averageVoteLoadingSpinner.clearAnimation();
        averageVoteLoadingSpinner.setVisibility(View.GONE);

        long amount = 1;
        long average = voteResult.getIndex() + 1;

        int answerIndex = 1;

        for (long answerId : answerStatistics.keySet()) {
            amount += answerStatistics.get(answerId);
            average += answerStatistics.get(answerId) * answerIndex;

            answerIndex++;
        }

        FiveMarksVoteResult averageVoteResult = amount == 0 ?
                FiveMarksVoteResult.VOTE_3 :
                FiveMarksVoteResult.getVoteResult((int) (average / amount) - 1);

        averageVoteSegmentedOvalView.setPercents(20 * averageVoteResult.getSegmentsAmount());
        averageVoteValueView.setText(getResources().getString(averageVoteResult.getVoteValueTextId()));
    }

    @UiThread
    void onGetStatisticsFail() {
        showMessage(getResources().getString(R.string.loading_failed), Type.ERROR);
    }

    @Click(R.id.continue_button)
    void onContinueClick() {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    private FiveMarksVoteResult getVoteResult(TranslatedQuestion question, long answerId) {
        for (int i = 0; i < 5; i++) {
            if (question.getAnswers().get(i).getId() == answerId) {
                return FiveMarksVoteResult.getVoteResult(i);
            }
        }

        throw new IllegalArgumentException("Unexpected value");
    }
}
