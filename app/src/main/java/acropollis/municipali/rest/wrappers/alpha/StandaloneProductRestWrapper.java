package acropollis.municipali.rest.wrappers.alpha;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.data.ScreenDensity;
import acropollis.municipali.data.backend.BackendInfo;
import acropollis.municipali.data.backend.CountryPlatformFullBackendInfo;
import acropollis.municipali.data.backend.StandaloneFullBackendInfo;
import acropollis.municipali.rest.raw.alpha.StandaloneProductRestService;
import acropollis.municipali.rest.raw.common.ImageRestService;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.utls.ScreenUtils;
import lombok.Data;

@EBean
public class StandaloneProductRestWrapper {
    @Bean
    ScreenUtils screenUtils;

    @RestService
    ImageRestService imageRestService;
    @RestService
    StandaloneProductRestService standaloneProductRestService;

    @Background
    public void getBackendInfoWithHighQualityBranding(String productId, RestListener<BackendInfo> listener) {
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

            StandaloneFullBackendInfo fullBackendInfo = standaloneProductRestService.getBackendInfo(productId);

            byte [] background = getBrandingBackground(
                    fullBackendInfo.getRootEndpoint(),
                    backgroundSize.getW(),
                    backgroundSize.getH()
            );

            byte [] icon = getBrandingIcon(fullBackendInfo.getRootEndpoint(), 300);

            BackendInfo backendInfo = new BackendInfo(); {
                backendInfo.setRootEndpoint(fullBackendInfo.getRootEndpoint());
                backendInfo.setBackground(background);
                backendInfo.setIcon(icon);
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

    private byte [] getBrandingIcon(String rootEndpoint, int size) {
        try {
            return imageRestService.getImage(rootEndpoint + "/branding/icon/" + size);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }

            throw e;
        }
    }
}
