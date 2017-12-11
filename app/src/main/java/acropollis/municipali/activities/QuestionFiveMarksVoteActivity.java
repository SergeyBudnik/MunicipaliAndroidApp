package acropollis.municipali.activities;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.R;
import acropollis.municipali.data.FiveMarksVoteResult;
import acropollis.municipali.service.UserService;
import acropollis.municipali.view.article.ArticleContainerView;
import acropollis.municipali.view.question.five_marks.FiveMarksVariantView;
import acropollis.municipalibootstrap.views.MunicipaliButtonView;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.rest_wrapper.answer.AnswerRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_five_marks_vote)
public class QuestionFiveMarksVoteActivity extends BaseActivity {
    @ViewById(R.id.article_container)
    ArticleContainerView articleContainerView;

    @ViewById(R.id.question_text)
    TextView questionTextView;
    @ViewById(R.id.negative_answer)
    TextView negativeAnswerView;
    @ViewById(R.id.positive_answer)
    TextView positiveAnswerView;

    @ViewById(R.id.vote_1)
    FiveMarksVariantView fiveMarksVariantView1;
    @ViewById(R.id.vote_2)
    FiveMarksVariantView fiveMarksVariantView2;
    @ViewById(R.id.vote_3)
    FiveMarksVariantView fiveMarksVariantView3;
    @ViewById(R.id.vote_4)
    FiveMarksVariantView fiveMarksVariantView4;
    @ViewById(R.id.vote_5)
    FiveMarksVariantView fiveMarksVariantView5;

    @ViewById(R.id.vote)
    MunicipaliButtonView voteView;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    long questionId;

    @Bean
    ArticleService articlesService;
    @Bean
    UserService userService;

    @Bean
    AnswerRestWrapper answerRestWrapper;

    private TranslatedQuestion question;
    private FiveMarksVoteResult voteResult;

    @AfterViews
    void init() {
        TranslatedArticle article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        articleContainerView.setArticle(article, false);

        question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());

        negativeAnswerView.setText(question.getAnswers().get(0).getText());
        positiveAnswerView.setText(question.getAnswers().get(4).getText());

        fiveMarksVariantView1.init(
                R.string.five_marks_strongly_disagree_text,
                it -> onVoteResultSelected(fiveMarksVariantView1, FiveMarksVoteResult.VOTE_1)
        );

        fiveMarksVariantView2.init(
                R.string.five_marks_disagree_text,
                it -> onVoteResultSelected(fiveMarksVariantView2, FiveMarksVoteResult.VOTE_2)
        );

        fiveMarksVariantView3.init(
                R.string.five_marks_neutral_text,
                it -> onVoteResultSelected(fiveMarksVariantView3, FiveMarksVoteResult.VOTE_3)
        );

        fiveMarksVariantView4.init(
                R.string.five_marks_agree_text,
                it -> onVoteResultSelected(fiveMarksVariantView4, FiveMarksVoteResult.VOTE_4)
        );

        fiveMarksVariantView5.init(
                R.string.five_marks_strongly_agree_text,
                it -> onVoteResultSelected(fiveMarksVariantView5, FiveMarksVoteResult.VOTE_5)
        );
    }

    private void onVoteResultSelected(FiveMarksVariantView fiveMarksVariantView, FiveMarksVoteResult voteResult) {
        unselectAllVoteResults();

        if (this.voteResult != voteResult) {
            this.voteResult = voteResult;

            fiveMarksVariantView.setSelected(true);

            voteView.setEnabled(true);
        } else {
            voteView.setEnabled(false);
        }
    }

    @Click(R.id.vote)
    void onVoteClick() {
        if (voteResult != null) {
            voteView.setLoading(true);

            vote();
        }
    }

    @Background
    void vote() {
        String authToken = userService.getCurrentUserAuthToken();
        ProductConfiguration productConfiguration = productConfigurationService.getProductConfiguration();

        if (authToken != null) {
            RestResult restResult = answerRestWrapper
                    .answerQuestion(
                            productConfiguration,
                            authToken,
                            articleId,
                            questionId,
                            question.getAnswers().get(voteResult.getIndex()).getId()
                    );

            if (restResult.isSuccessfull()) {
                Map<String, Serializable> extras = new HashMap<>(); {
                    extras.put("articleId", articleId);
                    extras.put("questionId", question.getId());
                }

                redirect(QuestionFiveMarksVoteResultActivity_.class, 0, 0, true, extras);
            } else {
                // ToDo: error
            }
        }
    }

    private void unselectAllVoteResults() {
        voteResult = null;

        fiveMarksVariantView1.setSelected(false);
        fiveMarksVariantView2.setSelected(false);
        fiveMarksVariantView3.setSelected(false);
        fiveMarksVariantView4.setSelected(false);
        fiveMarksVariantView5.setSelected(false);

        voteView.setEnabled(false);
    }
}
