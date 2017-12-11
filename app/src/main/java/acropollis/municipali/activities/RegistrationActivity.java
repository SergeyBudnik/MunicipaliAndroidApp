package acropollis.municipali.activities;

import android.content.Intent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.Collections;

import acropollis.municipali.R;
import acropollis.municipali.service.UserService;
import acropollis.municipalibootstrap.views.MunicipaliPopupMessageView.Type;
import acropollis.municipalidata.dto.article.ArticleType;

@EActivity(R.layout.activity_registration)
public class RegistrationActivity extends BaseActivity {
    private static final int LOGIN_TO_FACEBOOK_REQUEST = 0;
    private static final int LOGIN_TO_GOOGLE_PLUS_REQUEST = 1;
    private static final int LOGIN_SKIP_REQUEST = 2;

    @Bean
    UserService userService;

    @AfterViews
    void init() {
        if (userService.getCurrentUserAuthToken() != null) {
            redirect(
                    ArticlesListActivity_.class, 0, 0, true,
                    Collections.singletonMap("articlesType", ArticleType.NEWS)
            );
        }
    }

    @Click(R.id.sign_up_with_facebook_button)
    void onSignUpWithFacebookButton() {
        redirectForResult(RegistrationFacebookActivity_.class, 0, 0, LOGIN_TO_FACEBOOK_REQUEST);
    }

    @Click(R.id.skip_registration_button)
    void onSkipRegistrationButton() {
        redirectForResult(RegistrationSkipActivity_.class, 0, 0, LOGIN_SKIP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOGIN_TO_FACEBOOK_REQUEST:
                onLoginResult(resultCode == RESULT_OK, R.string.registration_fail_facebook);
                break;
            case LOGIN_TO_GOOGLE_PLUS_REQUEST:
                onLoginResult(resultCode == RESULT_OK, R.string.registration_fail_google_plus);
                break;
            case LOGIN_SKIP_REQUEST:
                onLoginResult(resultCode == RESULT_OK, R.string.loading_failed);
                break;
        }
    }

    private void onLoginResult(boolean successful, int loginFailureMessage) {
        if (successful) {
            redirect(
                    ArticlesListActivity_.class, 0, 0, true,
                    Collections.singletonMap("articlesType", ArticleType.NEWS)
            );
        } else {
            showMessage(getResources().getString(loginFailureMessage), Type.ERROR);
        }
    }
}
