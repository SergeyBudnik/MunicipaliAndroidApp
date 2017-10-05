package acropollis.municipali;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.view.AnswerListRowView;
import acropollis.municipali.view.AnswerListRowView_;
import acropollis.municipalibootstrap.views.MunicipaliLoadableImageView;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.article.ArticleImageService;
import acropollis.municipalidata.service.article.ArticleService;

@EActivity(R.layout.activity_multiple_variants_vote)
public class QuestionMultipleVariantsActivity extends BaseActivity {
    @ViewById(R.id.icon)
    MunicipaliLoadableImageView iconView;
    @ViewById(R.id.title)
    TextView titleView;
    @ViewById(R.id.question_text)
    TextView questionTextView;
    @ViewById(R.id.answers_list)
    LinearLayout answersListView;

//    @FragmentById(R.id.vote_buttons_layout)
//    QuestionVoteButtonsFragment questionVoteButtonsFragment;

    @Extra("articleId")
    long articleId;
    @Extra("questionId")
    Long questionId;

    @Bean
    ArticleService articlesService;
    @Bean
    ArticleRestWrapper articleRestWrapper;

    @Bean
    ArticleImageService articleImageService;

    private long currentAnswer = -1;

    @AfterViews
    void init() {
        TranslatedArticle article = articlesService.getArticle(
                productConfigurationService.getProductConfiguration(),
                articleId
        ).get();

        TranslatedQuestion question = article.getQuestions().get(questionId);

        titleView.setText(article.getTitle());
        questionTextView.setText(question.getText());

        ProductConfiguration configuration = productConfigurationService.getProductConfiguration();

        iconView.configure(
                String.valueOf(article.getId()),
                R.color.light_gray,
                () -> articleImageService.getArticleImage(configuration, article.getId()).orElse(null),
                () -> {
                    RestResult<byte []> image = articleRestWrapper.loadArticleImage(configuration, article.getId());

                    if (image.isSuccessfull()) {
                        return image.getData() != null ? image.getData() : new byte [0];
                    } else {
                        return null;
                    }
                }
        );

        Stream.of(question.getAnswers()).forEach(answer -> {
            AnswerListRowView row = AnswerListRowView_.build(QuestionMultipleVariantsActivity.this);

            row.bind(answer);
            //row.setOnClickListener(it -> onQuestionClick(question));

            answersListView.addView(row);
        });

//        questionVoteButtonsFragment.init(article, question, MultipleVariantsVoteResultActivity_.class, VoteHiddenResultActivity_.class);
    }

    @Click(R.id.back_button)
    void onBackButtonClick() {
        finish();
    }

//    void onAnswerClick(int position) {
//        answersListView.setItemSelected(position);
//
//        long newAnswer = answersListView.getElement(position).getId();
//
//        if (currentAnswer == newAnswer) {
//            currentAnswer = -1;
//
//            questionVoteButtonsFragment.removeAnswer();
//        } else {
//            currentAnswer = newAnswer;
//
//            questionVoteButtonsFragment.setAnswer(currentAnswer);
//        }
//    }
}
