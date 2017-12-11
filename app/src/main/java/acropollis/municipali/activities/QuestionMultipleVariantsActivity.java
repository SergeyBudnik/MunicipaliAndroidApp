package acropollis.municipali.activities;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import acropollis.municipali.R;
import acropollis.municipali.service.UserService;
import acropollis.municipali.view.article.ArticleContainerView;
import acropollis.municipali.view.rows.AnswerListRowView;
import acropollis.municipali.view.rows.AnswerListRowView_;
import acropollis.municipalibootstrap.views.MunicipaliButtonView;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.rest_wrapper.answer.AnswerRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleImageService;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_multiple_variants_vote)
public class QuestionMultipleVariantsActivity extends BaseActivity {
    @ViewById(R.id.article_container)
    ArticleContainerView articleContainerView;
    @ViewById(R.id.question_text)
    TextView questionTextView;
    @ViewById(R.id.answers_list)
    LinearLayout answersListView;
    @ViewById(R.id.vote)
    MunicipaliButtonView voteView;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    Long questionId;

    @Bean
    ArticleService articlesService;
    @Bean
    ArticleRestWrapper articleRestWrapper;
    @Bean
    AnswerRestWrapper answerRestWrapper;

    @Bean
    ArticleImageService articleImageService;
    @Bean
    UserService userService;

    List<AnswerListRowView> answerListRowViews = new ArrayList<>();

    private long currentAnswer = -1;

    @AfterViews
    void init() {
        TranslatedArticle article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        articleContainerView.setArticle(article, false);

        TranslatedQuestion question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());

        Stream.of(question.getAnswers()).forEach(answer -> {
            AnswerListRowView row = AnswerListRowView_.build(QuestionMultipleVariantsActivity.this); {
                answerListRowViews.add(row);
            }

            row.bind(answer);
            row.setOnClickListener(it -> {
                boolean selected = row.isSelected();

                Stream.of(answerListRowViews).forEach(r -> r.setSelected(false));

                if (!selected) {
                    row.setSelected(true);

                    currentAnswer = row.getAnswer().getId();

                    voteView.setEnabled(true);
                } else {
                    currentAnswer = -1;

                    voteView.setEnabled(false);
                }
            });

            answersListView.addView(row);
        });
    }

    @Click(R.id.vote)
    void onVoteClick() {
        if (currentAnswer != -1) {
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
                            currentAnswer
                    );

            if (restResult.isSuccessfull()) {
                Map<String, Serializable> extras = new HashMap<>(); {
                    extras.put("articleId", articleId);
                    extras.put("questionId", questionId);
                }

                redirect(QuestionMultipleVariantsResultActivity_.class, 0, 0, true, extras);
            } else {
                // ToDo: error
            }
        }
    }
}
