package acropollis.municipali;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;

import acropollis.municipali.data.user.User;
import acropollis.municipali.push.MunicipaliGcmMessages;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.omega.RegistrationRestWrapper;
import acropollis.municipali.service.UserService;
import acropollis.municipali.utls.ScreenUtils;

import static acropollis.municipali.utls.BitmapUtils.iconFromBytes;
import static acropollis.municipali.utls.BitmapUtils.iconToBytes;
import static acropollis.municipali.utls.BitmapUtils.scale;

@EActivity
public abstract class DoRegistrationActivity extends BaseActivity {
    private static final String ARTICLE_RELEASE_TOPIC = "ArticleRelease";

    @Bean
    RegistrationRestWrapper registrationRestWrapper;

    @Bean
    UserService userService;

    @Bean
    ScreenUtils screenUtils;

    private User user;
    private byte [] icon200Px;

    void registerUser(User user, byte [] icon200Px) {
        this.user = user;
        this.icon200Px = icon200Px;

        subscribeToGCM();
    }

    @UiThread
    void onSuccess(User user, String authToken, byte [] icon200Px) {
        userService.setCurrentUser(
                user,
                authToken,
                icon200Px == null ?
                        null :
                        iconToBytes(
                            scale(
                                iconFromBytes(icon200Px),
                                (int) (screenUtils.getScreenDensity().getPxInDp() * 50),
                                (int) (screenUtils.getScreenDensity().getPxInDp() * 50)
                            )
                )
        );

        finishRedirectForResult(0, 0, RESULT_OK);
   }

    @UiThread
    void onFailure() {
        finishRedirectForResult(0, 0, RESULT_CANCELED);
    }

    @Receiver(actions = MunicipaliGcmMessages.TOKEN_OBTAINED_FAILURE)
    void onTokenObtainedFailure() {
        onFailure();
    }

    private void subscribeToGCM() {
        FirebaseMessaging.getInstance().subscribeToTopic(
                productConfigurationService.getProductConfiguration().getProductId() +
                "_" +
                ARTICLE_RELEASE_TOPIC
        );

        String token = FirebaseInstanceId.getInstance().getToken();

        if (user.getUserId().getUserId() == null) {
            user.getUserId().setUserId(token);
        }

        user.getUserServiceInfo().setGmsToken(token);

        registrationRestWrapper.register(user, new RestListener<String>() {
            @Override
            public void onSuccess(String authToken) {
                DoRegistrationActivity.this.onSuccess(user, authToken, icon200Px);
            }

            @Override
            public void onFailure() {
                DoRegistrationActivity.this.onFailure();
            }
        });
    }
}
