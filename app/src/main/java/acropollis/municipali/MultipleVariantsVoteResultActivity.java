package acropollis.municipali;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.bootstrap.view.MunicipaliLayoutListView;
import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap_adapter.AnswerWithResultBootstrapAdapter;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.dto.article.question.answer.AnswerResult;
import acropollis.municipalidata.dto.article.question.answer.TranslatedAnswer;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.omega.ArticlesRestWrapper;
import acropollis.municipali.service.ArticlesService;
import acropollis.municipali.service.UserAnswerService;

@EActivity(R.layout.activity_multiple_variants_vote_result)
public class MultipleVariantsVoteResultActivity extends BaseActivity {
    @ViewById(R.id.article_info)
    MunicipaliRowView articleInfoView;
    @ViewById(R.id.question_text)
    TextView questionTextView;
    @ViewById(R.id.answers_list)
    MunicipaliLayoutListView answersListView;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    long questionId;

    @Bean
    ArticlesService articlesService;
    @Bean
    ArticlesRestWrapper articlesRestWrapper;
    @Bean
    UserAnswerService userAnswerService;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;
    @Bean
    AnswerWithResultBootstrapAdapter answerBootstrapAdapter;

    @AfterViews
    void init() {
        final TranslatedArticle article = articlesService.getArticle(articleId);

        articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, false));

        final TranslatedQuestion question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());

        final long currentAnswer = userAnswerService.getAnswer(articleId, questionId);

        articlesRestWrapper.getAnswer(articleId, questionId, new RestListener<Map<Long, Long>>() {
            @Override
            public void onSuccess(Map<Long, Long> answers) {
                populateList(article, question, answers, currentAnswer);
            }

            @Override
            public void onFailure() {
                onAnswersLoadingFailed();
            }
        });
    }

    @UiThread
    void onAnswersLoadingFailed() {
        showMessage(getResources().getString(R.string.loading_failed));
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        finish();
    }

    @Click(R.id.continue_button)
    void onContinueButtonClick() {
        finish();
    }

    @UiThread
    void populateList(TranslatedArticle article, TranslatedQuestion question, Map<Long, Long> results, long currentAnswer) {
        List<AnswerResult> answers = getAnswers(question.getAnswers(), results, currentAnswer);

        answersListView.setElements(
                answerBootstrapAdapter.getAnswersRows(article.getId(), question.getId(), answers),
                new MunicipaliLayoutListView.RowClickListener() {
                    @Override
                    public void onClick(int position, MunicipaliRowData rowData) {
                        /* Do nothing */
                    }
                }
        );

        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).getAnswerId() == currentAnswer) {
                answersListView.setItemSelected(i);
                break;
            }
        }
    }

    private List<AnswerResult> getAnswers(List<TranslatedAnswer> answers, Map<Long, Long> results, long currentAnswer) {
        List<AnswerResult> answerResults = new ArrayList<>();

        long totalVotes = 1;

        for (long answerId : results.keySet()) {
            totalVotes += results.get(answerId);
        }

        for (TranslatedAnswer answer : answers) {
            long resultsAmount;

            if (results.containsKey(answer.getId())) {
                resultsAmount = answer.getId() == currentAnswer ?
                        results.get(answer.getId()) + 1 :
                        results.get(answer.getId());
            } else {
                resultsAmount = 0;
            }

            AnswerResult answerResult = new AnswerResult(); {
                answerResult.setAnswerId(answer.getId());
                answerResult.setText(answer.getText());
                answerResult.setPercents(100 * resultsAmount / totalVotes);
            }

            answerResults.add(answerResult);
        }

        return answerResults;
    }
}
