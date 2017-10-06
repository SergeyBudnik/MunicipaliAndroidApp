package acropollis.municipali;

import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.bootstrap.view.MunicipaliRowView;
import acropollis.municipali.bootstrap_adapter.ArticleBootstrapAdapter;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipali.fragments.question.vote.QuestionVoteButtonsFragment;
import acropollis.municipali.data.FiveMarksVoteResult;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_five_marks_vote)
public class QuestionFiveMarksVoteActivity extends BaseActivity {
    @ViewById(R.id.article_info)
    MunicipaliRowView articleInfoView;

    @ViewById(R.id.question_text)
    TextView questionTextView;
    @ViewById(R.id.negative_answer)
    TextView negativeAnswerView;
    @ViewById(R.id.positive_answer)
    TextView positiveAnswerView;

    @ViewById(R.id.vote_1_unselected_underscore)
    View voteUnselectedUnderscoreView1;
    @ViewById(R.id.vote_1_selected_underscore)
    View voteSelectedUnderscoreView1;    
    @ViewById(R.id.vote_1_unselected_circle)
    View voteUnselectedCircleView1;
    @ViewById(R.id.vote_1_selected_circle)
    View voteSelectedCircleView1;

    @ViewById(R.id.vote_2_unselected_underscore)
    View voteUnselectedUnderscoreView2;
    @ViewById(R.id.vote_2_selected_underscore)
    View voteSelectedUnderscoreView2;
    @ViewById(R.id.vote_2_unselected_circle)
    View voteUnselectedCircleView2;
    @ViewById(R.id.vote_2_selected_circle)
    View voteSelectedCircleView2;

    @ViewById(R.id.vote_3_unselected_underscore)
    View voteUnselectedUnderscoreView3;
    @ViewById(R.id.vote_3_selected_underscore)
    View voteSelectedUnderscoreView3;
    @ViewById(R.id.vote_3_unselected_circle)
    View voteUnselectedCircleView3;
    @ViewById(R.id.vote_3_selected_circle)
    View voteSelectedCircleView3;

    @ViewById(R.id.vote_4_unselected_underscore)
    View voteUnselectedUnderscoreView4;
    @ViewById(R.id.vote_4_selected_underscore)
    View voteSelectedUnderscoreView4;
    @ViewById(R.id.vote_4_unselected_circle)
    View voteUnselectedCircleView4;
    @ViewById(R.id.vote_4_selected_circle)
    View voteSelectedCircleView4;

    @ViewById(R.id.vote_5_unselected_underscore)
    View voteUnselectedUnderscoreView5;
    @ViewById(R.id.vote_5_selected_underscore)
    View voteSelectedUnderscoreView5;
    @ViewById(R.id.vote_5_unselected_circle)
    View voteUnselectedCircleView5;
    @ViewById(R.id.vote_5_selected_circle)
    View voteSelectedCircleView5;

//    @FragmentById(R.id.vote_buttons_layout)
//    QuestionVoteButtonsFragment questionVoteButtonsFragment;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    long questionId;

    @Bean
    ArticleService articlesService;

    @Bean
    ArticleBootstrapAdapter articleBootstrapAdapter;

    private TranslatedQuestion question;
    private FiveMarksVoteResult voteResult;

    @AfterViews
    void init() {
        TranslatedArticle article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, false));

        question = article.getQuestions().get(questionId);

        questionTextView.setText(question.getText());

        negativeAnswerView.setText(question.getAnswers().get(0).getText());
        positiveAnswerView.setText(question.getAnswers().get(4).getText());

        //questionVoteButtonsFragment.init(article, question, FiveMarksVoteResultActivity_.class, VoteHiddenResultActivity_.class);
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        finish();
    }
    
    @Click(R.id.vote_1)
    void onVote1Click() {
        if (voteResult == FiveMarksVoteResult.VOTE_1) {
            unselectAllVoteResults();
        } else {
            selectVoteResult(FiveMarksVoteResult.VOTE_1, voteSelectedUnderscoreView1, voteSelectedCircleView1);
        }
    }

    @Click(R.id.vote_2)
    void onVote2Click() {
        if (voteResult == FiveMarksVoteResult.VOTE_2) {
            unselectAllVoteResults();
        } else {
            selectVoteResult(FiveMarksVoteResult.VOTE_2, voteSelectedUnderscoreView2, voteSelectedCircleView2);
        }
    }

    @Click(R.id.vote_3)
    void onVote3Click() {
        if (voteResult == FiveMarksVoteResult.VOTE_3) {
            unselectAllVoteResults();
        } else {
            selectVoteResult(FiveMarksVoteResult.VOTE_3, voteSelectedUnderscoreView3, voteSelectedCircleView3);
        }
    }

    @Click(R.id.vote_4)
    void onVote4Click() {
        if (voteResult == FiveMarksVoteResult.VOTE_4) {
            unselectAllVoteResults();
        } else {
            selectVoteResult(FiveMarksVoteResult.VOTE_4, voteSelectedUnderscoreView4, voteSelectedCircleView4);
        }
    }

    @Click(R.id.vote_5)
    void onVote5Click() {
        if (voteResult == FiveMarksVoteResult.VOTE_5) {
            unselectAllVoteResults();
        } else {
            selectVoteResult(FiveMarksVoteResult.VOTE_5, voteSelectedUnderscoreView5, voteSelectedCircleView5);
        }
    }

    private void selectVoteResult(FiveMarksVoteResult voteResult,
                                  View voteSelectedUnderscoreView,
                                  View voteSelectedCircleView) {

        unselectAllVoteResults();

        this.voteResult = voteResult;

        voteSelectedUnderscoreView.setVisibility(View.VISIBLE);
        voteSelectedCircleView.setVisibility(View.VISIBLE);

        //questionVoteButtonsFragment.setAnswer(question.getAnswers().get(voteResult.getIndex()).getId());
    }

    private void unselectAllVoteResults() {
        voteResult = null;

        voteSelectedUnderscoreView1.setVisibility(View.GONE);
        voteSelectedCircleView1.setVisibility(View.GONE);

        voteSelectedUnderscoreView2.setVisibility(View.GONE);
        voteSelectedCircleView2.setVisibility(View.GONE);

        voteSelectedUnderscoreView3.setVisibility(View.GONE);
        voteSelectedCircleView3.setVisibility(View.GONE);

        voteSelectedUnderscoreView4.setVisibility(View.GONE);
        voteSelectedCircleView4.setVisibility(View.GONE);

        voteSelectedUnderscoreView5.setVisibility(View.GONE);
        voteSelectedCircleView5.setVisibility(View.GONE);

        //questionVoteButtonsFragment.removeAnswer();
    }
}
