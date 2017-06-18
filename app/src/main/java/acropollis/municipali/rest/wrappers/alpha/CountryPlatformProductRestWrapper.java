package acropollis.municipali.rest.wrappers.alpha;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import acropollis.municipali.data.ScreenDensity;
import acropollis.municipali.data.backend.BackendInfo;
import acropollis.municipali.data.backend.CountryPlatformFullBackendInfo;
import acropollis.municipali.data.city.City;
import acropollis.municipali.data.country.Country;
import acropollis.municipali.rest.raw.alpha.AlphaRestConfiguration;
import acropollis.municipali.rest.raw.alpha.CountryPlatformProductRestService;
import acropollis.municipali.rest.raw.common.ImageRestService;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.utls.ScreenUtils;
import lombok.Data;

@EBean
public class CountryPlatformProductRestWrapper {
    @Bean
    ScreenUtils screenUtils;

    @RestService
    CountryPlatformProductRestService countryPlatformProductRestService;
    @RestService
    ImageRestService imageRestService;

    @Background
    public void getCountry(String productId, RestListener<Country> listener) {
        try {
            listener.onStart();

            Country country = countryPlatformProductRestService.getCountry(productId);

            listener.onSuccess(country);
        } catch (Exception e) {
            listener.onFailure();
        }
    }

    @Background
    public void getCityIcon(Country country, City city, RestListener<byte []> listener) {
        try {
            listener.onStart();

            byte [] icon = imageRestService.getImage(
                    AlphaRestConfiguration.ALPHA_BASE_URL +
                            String.format(
                                    Locale.ENGLISH,
                                    "/user/country/%d/city/%d/icon/%d",
                                    country.getId(),
                                    city.getId(),
                                    screenUtils.dpToPx(50)) // ToDo
            );

            listener.onSuccess(icon);
        } catch (Exception e) {
            listener.onFailure();
        }
    }

    @Background
    public void getBackendInfoWithHighQualityBranding(String productId, long cityId, RestListener<BackendInfo> listener) {
        @Data
        class BackgroundSize {
            int w;
            int h;

            private BackgroundSize(int w, int h) {
                this.w = w;
                this.h = h;
            }
        }

        try {
            Map<ScreenDensity, BackgroundSize> backgroundSizes = new HashMap<>(); {
                backgroundSizes.put(ScreenDensity.LDPI, new BackgroundSize(240, 320));
                backgroundSizes.put(ScreenDensity.MDPI, new BackgroundSize(320, 480));
                backgroundSizes.put(ScreenDensity.HDPI, new BackgroundSize(1440, 2560));
                backgroundSizes.put(ScreenDensity.XHDPI, new BackgroundSize(768, 1280));
                backgroundSizes.put(ScreenDensity.XXHDPI, new BackgroundSize(1080, 1920));
                backgroundSizes.put(ScreenDensity.XXXHDPI, new BackgroundSize(1440, 2560));
            }

            BackgroundSize backgroundSize = backgroundSizes.get(screenUtils.getScreenDensity());

            listener.onStart();

            CountryPlatformFullBackendInfo fullBackendInfo = countryPlatformProductRestService.getBackendInfo(productId, cityId);
            byte [] background = getBrandingBackground(
                    fullBackendInfo.getRootEndpoint(),
                    backgroundSize.getW(),
                    backgroundSize.getH()
            );

            BackendInfo backendInfo = new BackendInfo(); {
                backendInfo.setRootEndpoint(fullBackendInfo.getRootEndpoint());
                backendInfo.setBackground(background);
            }

            listener.onSuccess(backendInfo);
        } catch (Exception e) {
            listener.onFailure();
        }
    }

    private byte [] getBrandingBackground(String rootEndpoint, int w, int h) {
        try {
            return imageRestService.getImage(rootEndpoint + "/branding/background/" + w + "/" + h);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }

            throw e;
        }
    }
}
