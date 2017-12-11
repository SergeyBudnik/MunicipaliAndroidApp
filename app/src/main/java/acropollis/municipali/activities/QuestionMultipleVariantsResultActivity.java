package acropollis.municipali.activities;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Objects;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import acropollis.municipali.R;
import acropollis.municipali.view.article.ArticleContainerView;
import acropollis.municipali.view.rows.AnswerResultListRowView;
import acropollis.municipali.view.rows.AnswerResultListRowView_;
import acropollis.municipalibootstrap.views.MunicipaliButtonView;
import acropollis.municipalibootstrap.views.MunicipaliPopupMessageView.Type;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.dto.article.question.answer.TranslatedAnswer;
import acropollis.municipalidata.rest_wrapper.answer.AnswerRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.answer.UserAnswersService;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_multiple_variants_vote_result)
public class QuestionMultipleVariantsResultActivity extends BaseActivity {
    @ViewById(R.id.article_container)
    ArticleContainerView articleContainerView;
    @ViewById(R.id.question_text)
    TextView questionTextView;
    @ViewById(R.id.answers_list)
    LinearLayout answersListView;
    @ViewById(R.id.continue_button)
    MunicipaliButtonView continueView;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    long questionId;

    @Bean
    AnswerRestWrapper answerRestWrapper;

    @Bean
    ArticleService articlesService;
    @Bean
    ArticleRestWrapper articlesRestWrapper;
    @Bean
    UserAnswersService userAnswersService;

    private TranslatedArticle article;
    private TranslatedQuestion question;

    private List<AnswerResultListRowView> rows = new ArrayList<>();

    @AfterViews
    void init() {
        ProductConfiguration configuration = productConfigurationService.getProductConfiguration();

        article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        articleContainerView.setArticle(article, false);

        question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());

        final long currentAnswer = userAnswersService
                .getAnswer(configuration, articleId, questionId)
                .orElseThrow(RuntimeException::new);

        loadAnswers(currentAnswer);

        populateList(question);
    }

    @Background
    void loadAnswers(long currentAnswer) {
        ProductConfiguration configuration = productConfigurationService.getProductConfiguration();

        RestResult<Map<Long, Long>> results = answerRestWrapper.getAnswer(configuration, articleId, questionId);

        if (results.isSuccessfull()) {
            fillList(article, question, results.getData(), currentAnswer);
        }
    }

    @UiThread
    void onAnswersLoadingFailed() {
        showMessage(getResources().getString(R.string.loading_failed), Type.ERROR);
    }

    @Click(R.id.continue_button)
    void onContinueButtonClick() {
        finish();
    }

    @UiThread
    void populateList(TranslatedQuestion question) {
        for (TranslatedAnswer answer : question.getAnswers()) {
            AnswerResultListRowView row = AnswerResultListRowView_.build(QuestionMultipleVariantsResultActivity.this); {
                row.bind(answer);
            }

            rows.add(row);

            answersListView.addView(row);
        }
    }

    @UiThread
    void fillList(TranslatedArticle article, TranslatedQuestion question, Map<Long, Long> results, long currentAnswer) {
        long total = 0;

        for (Long resultId : results.keySet()) {
            total += results.get(resultId);
        }

        for (TranslatedAnswer answer : question.getAnswers()) {
            Optional<AnswerResultListRowView> row = Stream.of(rows)
                    .filter(it -> it.getAnswer().getId() == answer.getId())
                    .findSingle();

            if (row.isPresent()) {
                Long result = results.get(answer.getId());

                row.get().setPercents(total == 0 || result == null ? 0 : (int) (100 * result / total));
                row.get().setSelected(Objects.equals(answer.getId(), currentAnswer));
            }
        }
    }
}
