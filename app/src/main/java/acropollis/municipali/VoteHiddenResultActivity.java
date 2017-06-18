package acropollis.municipali;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipali.data.article.TranslatedArticle;
import acropollis.municipali.data.article.question.TranslatedQuestion;
import acropollis.municipali.service.ArticlesService;

@EActivity(R.layout.activity_vote_hidden_result)
public class VoteHiddenResultActivity extends BaseActivity {
    @ViewById(R.id.article_info)
    MunicipaliRowView articleInfoView;
    @ViewById(R.id.question_text)
    TextView questionTextView;

    @Bean
    ArticlesService articlesService;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    long questionId;

    @AfterViews
    void init() {
        TranslatedArticle article = articlesService.getArticle(articleId);

        articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, false));

        TranslatedQuestion question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    @Click(R.id.continue_button)
    void onContinueClick() {
        finishRedirectForResult(0, 0, RESULT_OK);
    }
}
