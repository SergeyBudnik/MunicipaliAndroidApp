package acropollis.municipali.fragments.question.vote;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import acropollis.municipali.BaseActivity;
import acropollis.municipali.R;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipali.service.UserAnswerService;
import acropollis.municipali.service.UserService;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;

@EFragment(R.layout.fragment_question_vote_buttons)
public class QuestionVoteButtonsFragment extends Fragment {
    @ViewById(R.id.vote_button)
    View voteButtonView;
    @ViewById(R.id.skip_button)
    View skipButtonView;
    @ViewById(R.id.loading_button)
    View loadingButtonView;

    @ViewById(R.id.loading_button_spinner)
    View loadingButtonSpinnerView;

    @Bean
    UserAnswerService userAnswersService;
    @Bean
    UserService userService;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    @AnimationRes(R.anim.spinner)
    Animation spinnerAnimation;

    private AtomicBoolean isVoting = new AtomicBoolean(false);

    private Class<? extends BaseActivity> successRedirectTarget;
    private Class<? extends BaseActivity> hiddenRedirectTarget;
    private TranslatedArticle article;
    private TranslatedQuestion question;
    private long answerId = -1;

    public void init(
            TranslatedArticle article,
            TranslatedQuestion question,
            Class<? extends BaseActivity> successRedirectTarget,
            Class<? extends BaseActivity> hiddenRedirectTarget) {

        this.article = article;
        this.question = question;
        this.successRedirectTarget = successRedirectTarget;
        this.hiddenRedirectTarget = hiddenRedirectTarget;
    }

    public void setAnswer(long answerId) {
        this.answerId = answerId;

        voteButtonView.setVisibility(View.VISIBLE);
        skipButtonView.setVisibility(View.GONE);
    }

    public void removeAnswer() {
        this.answerId = -1;

        voteButtonView.setVisibility(View.INVISIBLE);
        skipButtonView.setVisibility(View.VISIBLE);
    }

    @Click(R.id.vote_button)
    void onVoteClick() {
        synchronized (this) {
            if (isVoting.get()) {
                return;
            }

            voteButtonView.setVisibility(View.INVISIBLE);
            loadingButtonView.setVisibility(View.VISIBLE);

            loadingButtonSpinnerView.startAnimation(spinnerAnimation);

            isVoting.set(true);

            String authToken =
                    userService.getCurrentUserAuthToken() != null ?
                            userService.getCurrentUserAuthToken() :
                            "";

//            articlesRestWrapper.answerQuestion(
//                    authToken, article.getId(), question.getId(), answerId,
//                    new RestListener<Void>() {
//                        @Override
//                        public void onSuccess(Void o) {
//                            onVoteSuccess();
//                        }
//
//                        @Override
//                        public void onFailure() {
//                            onVoteFail();
//                        }
//                    });
        }
    }

    @UiThread
    void onVoteSuccess() {
        userAnswersService.addAnswer(article.getId(), question.getId(), answerId);

        if (question.isShowResult()) {
            doVoteRedirect(successRedirectTarget);
        } else {
            doVoteRedirect(hiddenRedirectTarget);
        }
    }

    @UiThread
    void onVoteFail() {
        voteButtonView.setVisibility(View.VISIBLE);
        loadingButtonView.setVisibility(View.INVISIBLE);

        isVoting.set(false);

        ((BaseActivity) getActivity()).showMessage(getResources().getString(R.string.voting_failed));
    }

    @Click(R.id.skip_button)
    void onSkipClick() {
        userAnswersService.addAnswer(article.getId(), question.getId(), 0);

        getActivity().finish();
    }

    protected void doVoteRedirect(Class<? extends BaseActivity> redirectTo) {
        Map<String, Serializable> extras = new HashMap<>(); {
            extras.put("articleId", article.getId());
            extras.put("questionId", question.getId());
        }

        ((BaseActivity) getActivity()).redirect(redirectTo, 0, 0, true, extras);
    }
}
