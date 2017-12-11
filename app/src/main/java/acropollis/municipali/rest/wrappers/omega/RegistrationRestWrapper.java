package acropollis.municipali.rest.wrappers.omega;

import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import acropollis.municipali.data.user.User;
import acropollis.municipali.rest.raw.omega.UserRestService;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.service.configuration.ConfigurationService;

@EBean
public class RegistrationRestWrapper {
    @Bean
    ProductConfigurationService productConfigurationService;
    @Bean
    ConfigurationService configurationService;

    @RestService
    UserRestService userRestService;

    @Background
    public void register(User user, RestListener<String> listener) {
        try {
            listener.onStart();

            ProductConfiguration productConfiguration = productConfigurationService.getProductConfiguration();

            String authToken = userRestService.register(
                    configurationService.getServerRootUrl(productConfiguration).get(),
                    user
            ).getAuthToken();

            listener.onSuccess(authToken);
        } catch (Exception e) {
            if (productConfigurationService.getProductConfiguration().isQa()) {
                throw new RuntimeException(e);
            } else {
                Log.e("RegistrationRestWrapper", "Registration failed", e);

                listener.onFailure();
            }
        }
    }
}
