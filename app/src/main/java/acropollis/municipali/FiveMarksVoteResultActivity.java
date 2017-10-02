package acropollis.municipali;

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

import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.UserAnswerService;
import acropollis.municipali.view.common.SegmentedView;
import acropollis.municipalidata.rest_wrapper.answer.AnswerRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleService;
import acropollis.municipali.data.FiveMarksVoteResult;

@EActivity(R.layout.activity_five_marks_vote_result)
public class FiveMarksVoteResultActivity extends BaseActivity {
    @ViewById(R.id.article_info)
    MunicipaliRowView articleInfoView;

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
    UserAnswerService userAnswerService;

    @Bean
    ArticleRestWrapper articleRestWrapper;

    @AfterViews
    void init() {
        TranslatedArticle article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        //articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, false));

        TranslatedQuestion question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());

        final FiveMarksVoteResult voteResult = getVoteResult(question, userAnswerService.getAnswer(articleId, questionId));

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
        showMessage(getResources().getString(R.string.loading_failed));
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        finishRedirectForResult(0, 0, RESULT_OK);
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
