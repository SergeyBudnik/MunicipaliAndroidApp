package acropollis.municipali;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.bootstrap.view.MunicipaliLayoutListView;
import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap_adapter.AnswerBootstrapAdapter;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipali.fragments.question.vote.QuestionVoteButtonsFragment;
import acropollis.municipali.service.ArticlesService;

@EActivity(R.layout.activity_multiple_variants_vote)
public class MultipleVariantsVoteActivity extends BaseActivity {
    @ViewById(R.id.article_info)
    MunicipaliRowView articleInfoView;
    @ViewById(R.id.question_text)
    TextView questionTextView;
    @ViewById(R.id.answers_list)
    MunicipaliLayoutListView answersListView;

    @FragmentById(R.id.vote_buttons_layout)
    QuestionVoteButtonsFragment questionVoteButtonsFragment;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    Long questionId;

    @Bean
    ArticlesService articlesService;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;
    @Bean
    AnswerBootstrapAdapter answerBootstrapAdapter;

    private long currentAnswer = -1;

    @AfterViews
    void init() {
        TranslatedArticle article = articlesService.getArticle(articleId);

        articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, false));

        TranslatedQuestion question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());

        answersListView.setElements(
                answerBootstrapAdapter.getAnswersRows(articleId, questionId, question.getAnswers()),
                new MunicipaliLayoutListView.RowClickListener() {
                    @Override
                    public void onClick(int position, MunicipaliRowData rowData) {
                        onAnswerClick(position);
                    }
                }
        );

        questionVoteButtonsFragment.init(article, question, MultipleVariantsVoteResultActivity_.class, VoteHiddenResultActivity_.class);
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        finish();
    }

    void onAnswerClick(int position) {
        answersListView.setItemSelected(position);

        long newAnswer = answersListView.getElement(position).getId();

        if (currentAnswer == newAnswer) {
            currentAnswer = -1;

            questionVoteButtonsFragment.removeAnswer();
        } else {
            currentAnswer = newAnswer;

            questionVoteButtonsFragment.setAnswer(currentAnswer);
        }
    }
}
