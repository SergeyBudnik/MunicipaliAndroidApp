package acropollis.municipali;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.binders.MenuBinder;
import acropollis.municipali.bootstrap_adapter.QuestionBootstrapAdapter;
import acropollis.municipali.data.AnswerStatus;
import acropollis.municipali.service.UserAnswerService;
import acropollis.municipali.view.ArticleInfoView;
import acropollis.municipali.view.HeaderView;
import acropollis.municipali.view.MenuView;
import acropollis.municipali.view.QuestionListRowView;
import acropollis.municipali.view.QuestionListRowView_;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.service.article.ArticleImageService;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_article)
public class ArticleActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int REDIRECT_FOR_VOTE = 1;

    @ViewById(R.id.root)
    DrawerLayout rootView;
    @ViewById(R.id.menu)
    MenuView menuView;

    @ViewById(R.id.header)
    HeaderView headerView;
    @ViewById(R.id.article_info)
    ArticleInfoView articleInfoView;
    @ViewById(R.id.article_text)
    TextView articleTextView;
    @ViewById(R.id.questions_list)
    LinearLayout questionsListView;

    @ViewById(R.id.youtube_view)
    YouTubePlayerView youTubeView;

    @Extra("articleId")
    Long articleId;

    @Bean
    ArticleService articlesService;
    @Bean
    UserAnswerService userAnswerService;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @Bean
    ArticleImageService articleImageService;

    @Bean
    QuestionBootstrapAdapter questionBootstrapAdapter;

    @Bean
    MenuBinder menuBinder;

    private TranslatedArticle article;

    @AfterViews
    void init() {
        headerView.setStyle(HeaderView.Style.TRANSPARENT);
        headerView.setTitle(R.string.article);
        headerView.setLeftButton(R.drawable.back_button, it -> finishRedirectForResult(0, 0, RESULT_OK));

        article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        articleInfoView.setArticle(article);
        articleTextView.setText(article.getText());

        questionsListView.removeAllViews();

        Stream.of(article.getQuestions().values()).forEach(question -> {
            QuestionListRowView row = QuestionListRowView_.build(ArticleActivity.this);

            row.bind(question);
            row.setOnClickListener(it -> onQuestionClick(question));

            questionsListView.addView(row);
        });

        youTubeView.initialize(Config.YOUTUBE_API_KEY, this);

        if (article.getVideo() != null) {
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.cueVideo("fhWaJi1Hsfo"); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onBackPressed() {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REDIRECT_FOR_VOTE) {
            //articleInfoView.bind(articleBootstrapAdapter.getArticleRowInfo(article, true));
//            questionsListView.setElements(
//                    questionBootstrapAdapter.getQuestionsRows(article),
//                    new MunicipaliLayoutListView.RowClickListener() {
//                        @Override
//                        public void onClick(int position, MunicipaliRowData rowData) {
//                            onQuestionClick(rowData);
//                        }
//                    }
//            );
        }
    }

    private void onQuestionClick(TranslatedQuestion question) {
        Class<? extends BaseActivity> questionActivity;

        boolean isAnswered = userAnswerService.getAnswerStatus(articleId, question.getId()) == AnswerStatus.ANSWERED;
        boolean isHidden = !question.isShowResult();

        switch (question.getAnswerType()) {
            case FIVE_POINTS:
                if (!isAnswered) {
                    questionActivity = QuestionFiveMarksVoteActivity_.class;
                } else if (!isHidden) {
                    questionActivity = QuestionFiveMarksVoteResultActivity_.class;
                } else {
                    questionActivity = VoteHiddenResultActivity_.class;
                }
                break;
            case THREE_VARIANTS:
            case DYCHOTOMOUS:
                if (!isAnswered) {
                    questionActivity = QuestionMultipleVariantsActivity_.class;
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
        extras.put("questionId", question.getId());

        redirectForResult(questionActivity, 0, 0, REDIRECT_FOR_VOTE, extras);
    }
}
