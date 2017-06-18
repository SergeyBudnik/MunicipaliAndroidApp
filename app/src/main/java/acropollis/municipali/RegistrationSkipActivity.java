package acropollis.municipali;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.bootstrap.view.MunicipaliLoadingView;
import acropollis.municipali.data.user.User;
import acropollis.municipali.data.user.UserId;

@EActivity(R.layout.activity_loading)
public class RegistrationSkipActivity extends DoRegistrationActivity {
    @ViewById(R.id.loading)
    MunicipaliLoadingView loadingView;

    @AfterViews
    void init() {
        loadingView.bind(new MunicipaliLoadingView.Loader() {
            @Override
            public void onStart() {
                User user = new User(); {
                    user.getUserId().setLoginType(UserId.LoginType.NONE);
                }

                registerUser(user, null);
            }

            @Override
            public MunicipaliLoadingView.LoadingFailureAction onFailure() {
                return MunicipaliLoadingView.LoadingFailureAction.REDIRECT;
            }
        });

        loadingView.startLoading();
    }
}
