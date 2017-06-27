package acropollis.municipali.rest.wrappers.omega;

import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import acropollis.municipali.data.user.User;
import acropollis.municipali.rest.raw.omega.UserRestService;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.BackendInfoService;

@EBean
public class RegistrationRestWrapper {
    @Bean
    BackendInfoService backendInfoService;

    @RestService
    UserRestService userRestService;

    @Background
    public void register(User user, RestListener<String> listener) {
        try {
            listener.onStart();

            String authToken = userRestService.register(
                    backendInfoService.getBackendInfo().getRootEndpoint(),
                    user
            ).getAuthToken();

            listener.onSuccess(authToken);
        } catch (Exception e) {
            Log.e("RegistrationRestWrapper", "Registration failed", e);

            listener.onFailure();
        }
    }
}
