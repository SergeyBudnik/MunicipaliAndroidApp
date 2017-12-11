package acropollis.municipali.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.Arrays;

import acropollis.municipali.R;
import acropollis.municipali.bootstrap.view.MunicipaliLoadingView;
import acropollis.municipali.data.user.User;
import acropollis.municipali.data.user.UserDetailsInfo;
import acropollis.municipali.data.user.UserId;
import acropollis.municipali.rest.raw.downstream.FacebookProfileImageRestService;

import static acropollis.municipali.utls.BitmapUtils.centeredCrop;
import static acropollis.municipali.utls.BitmapUtils.iconFromBytes;
import static acropollis.municipali.utls.BitmapUtils.iconToBytes;
import static acropollis.municipali.utls.BitmapUtils.scale;

@EActivity(R.layout.activity_loading)
public class RegistrationFacebookActivity extends DoRegistrationActivity {
    @ViewById(R.id.loading)
    MunicipaliLoadingView loadingView;

    @RestService
    FacebookProfileImageRestService facebookProfileImageRestService;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @AfterViews
    void init() {
        loadingView.bind(new MunicipaliLoadingView.Loader() {
            @Override
            public void onStart() {
                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().logInWithReadPermissions(
                        RegistrationFacebookActivity.this,
                        Arrays.asList(
                                "public_profile",
                                "email"
                        )
                );
            }

            @Override
            public MunicipaliLoadingView.LoadingFailureAction onFailure() {
                return MunicipaliLoadingView.LoadingFailureAction.REDIRECT;
            }
        });

        loadingView.startLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                (jsonObject, response) -> {
                    try {
                        User user1 = new User();

                        user1.getUserId().setUserId(jsonObject.getString("id"));
                        user1.getUserId().setLoginType(UserId.LoginType.FACEBOOK);

                        if (jsonObject.has("name")) {
                            user1.getUserDetailsInfo().setName(jsonObject.getString("name"));
                        }

                        if (jsonObject.has("gender")) {
                            user1.getUserDetailsInfo().setGender(parseGender(jsonObject.getString("gender")));
                        }

                        user1.getUserDetailsInfo().setDateOfBirth(-1L);

                        if (jsonObject.has("email")) {
                            user1.getUserDetailsInfo().setEmail(jsonObject.getString("email"));
                        }

                        loadUserImage(user1);
                    } catch (Exception e) {
                        if (productConfigurationService.getProductConfiguration().isQa()) {
                            throw new RuntimeException(e);
                        } else {
                            finishRedirectForResult(0, 0, RESULT_CANCELED);
                        }
                    }
                });

        Bundle parameters = new Bundle();

        parameters.putString("fields", "id,name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Background
    void loadUserImage(User user) {
        try {
            byte [] image = facebookProfileImageRestService.getImage(user.getUserId().getUserId());

            Bitmap src = iconFromBytes(image);

            int size = Math.min(src.getWidth(), src.getHeight());

            registerUser(
                    user,
                    iconToBytes(scale(centeredCrop(src, size, size), 200, 200))
            );
        } catch (Exception e) {
            Crashlytics.logException(e);

            finishRedirectForResult(0, 0, RESULT_CANCELED);
        }
    }

    @UiThread
    void redirect() {
        finishRedirectForResult(0, 0, RESULT_OK);
    }

    private UserDetailsInfo.Gender parseGender(String gender) {
        if (gender != null) {
            switch (gender) {
                case "male":
                    return UserDetailsInfo.Gender.MALE;
                case "female":
                    return UserDetailsInfo.Gender.FEMALE;
                default:
                    return UserDetailsInfo.Gender.UNKNOWN;
            }
        } else {
            return UserDetailsInfo.Gender.UNKNOWN;
        }
    }
}
