package acropollis.municipali;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

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
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.bootstrap.view.MunicipaliLayoutListView;
import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipali.bootstrap_adapter.QuestionBootstrapAdapter;
import acropollis.municipali.data.AnswerStatus;
import acropollis.municipali.data.article.TranslatedArticle;
import acropollis.municipali.data.article.question.TranslatedQuestion;
import acropollis.municipali.rest.wrappers.omega.ArticlesRestWrapper;
import acropollis.municipali.service.ArticlesService;
import acropollis.municipali.service.UserAnswerService;
import acropollis.municipali.view.question.ArticleCategoriesView;

@EActivity(R.layout.activity_article_questions_list)
public class ArticleQuestionsListActivity extends BaseActivity {
    private static final int REDIRECT_FOR_VOTE = 1;

    @ViewById(R.id.root)
    DrawerLayout rootView;

    @ViewById(R.id.article_info)
    MunicipaliRowView articleInfoView;
    @ViewById(R.id.article_categories)
    ArticleCategoriesView articleCategoriesView;
    @ViewById(R.id.article_text)
    TextView articleTextView;
    @ViewById(R.id.questions_list)
    MunicipaliLayoutListView questionsListView;

    @Extra("articleId")
    Long articleId;

    @Bean
    ArticlesService articlesService;
    @Bean
    UserAnswerService userAnswerService;

    @Bean
    ArticlesRestWrapper articlesRestWrapper;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;
    @Bean
    QuestionBootstrapAdapter questionBootstrapAdapter;

    @Bean
    MenuBinder menuBinder;

    private TranslatedArticle article;

    @AfterViews
    void init() {
        article = articlesService.getArticle(articleId);

        articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, true));
        articleCategoriesView.bind(article);
        articleTextView.setText(article.getText());

        questionsListView.setElements(
                questionBootstrapAdapter.getQuestionsRows(article),
                new MunicipaliLayoutListView.RowClickListener() {
                    @Override
                    public void onClick(int position, MunicipaliRowData rowData) {
                        onQuestionClick(rowData);
                    }
                }
        );
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

    void onQuestionClick(MunicipaliRowData questionRow) {
        TranslatedQuestion question = article.getQuestions().get(questionRow.getId());

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
                    questionActivity = MultipleVariantsVoteActivity_.class;
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
        extras.put("questionId", questionRow.getId());

        redirectForResult(questionActivity, 0, 0, REDIRECT_FOR_VOTE, extras);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REDIRECT_FOR_VOTE) {
            articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, true));
            questionsListView.setElements(
                    questionBootstrapAdapter.getQuestionsRows(article),
                    new MunicipaliLayoutListView.RowClickListener() {
                        @Override
                        public void onClick(int position, MunicipaliRowData rowData) {
                            onQuestionClick(rowData);
                        }
                    }
            );
        }
    }
}
