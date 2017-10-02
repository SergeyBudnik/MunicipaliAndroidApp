package acropollis.municipali.rest.wrappers.alpha;

import android.util.Log;

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
import acropollis.municipali.data.backend.ImageHostingInfo;
import acropollis.municipali.data.backend.StandaloneFullBackendInfo;
import acropollis.municipali.rest.raw.alpha.StandaloneProductRestService;
import acropollis.municipali.rest.raw.common.ImageRestService;
import acropollis.municipali.rest.raw.omega.ConfigurationRestService;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.utls.ScreenUtils;
import acropollis.municipalidata.service.configuration.ConfigurationService;
import lombok.Data;

@EBean
public class StandaloneProductRestWrapper {
    @Bean
    ScreenUtils screenUtils;

    @RestService
    ImageRestService imageRestService;
    @RestService
    StandaloneProductRestService standaloneProductRestService;
    @RestService
    ConfigurationRestService configurationRestService;

    @Bean
    ConfigurationService configurationService;

    @Bean
    ProductConfigurationService productConfigurationService;

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
                backgroundSizes.put(ScreenDensity.LDPI,    new BackgroundSize(120, 160));
                backgroundSizes.put(ScreenDensity.MDPI,    new BackgroundSize(160, 240));
                backgroundSizes.put(ScreenDensity.HDPI,    new BackgroundSize(240, 400));
                backgroundSizes.put(ScreenDensity.XHDPI,   new BackgroundSize(384, 640));
                backgroundSizes.put(ScreenDensity.XXHDPI,  new BackgroundSize(540, 960));
                backgroundSizes.put(ScreenDensity.XXXHDPI, new BackgroundSize(720, 1280));
            }

            BackgroundSize backgroundSize = backgroundSizes.get(screenUtils.getScreenDensity());

            listener.onStart();

            StandaloneFullBackendInfo fullBackendInfo = standaloneProductRestService.getBackendInfo(productId);

            ImageHostingInfo imageHostingInfo = configurationRestService.getImageHostingInfo(
                    fullBackendInfo.getRootEndpoint()
            );

            byte [] background = getBrandingBackground(
                    imageHostingInfo.getUrl(),
                    backgroundSize.getW(),
                    backgroundSize.getH()
            );

            byte [] icon = getBrandingIcon(imageHostingInfo.getUrl(), 300);

            BackendInfo backendInfo = new BackendInfo(); {
                backendInfo.setRootEndpoint(fullBackendInfo.getRootEndpoint());
                backendInfo.setImageHostingRootEndpoint(imageHostingInfo.getUrl());
                backendInfo.setBackground(background);
                backendInfo.setIcon(icon);
            }

            configurationService.setServerRootUrl(
                    productConfigurationService.getProductConfiguration(),
                    fullBackendInfo.getRootEndpoint()
            );

            configurationService.setImageHostingRootUrl(
                    productConfigurationService.getProductConfiguration(),
                    imageHostingInfo.getUrl()
            );

            listener.onSuccess(backendInfo);
        } catch (Exception e) {
            Log.e("SProductRestWrapper", "City icon loading failed", e);

            listener.onFailure();
        }
    }

    private byte [] getBrandingBackground(String rootEndpoint, int w, int h) {
        try {
            return imageRestService.getImage(rootEndpoint + "/branding/background/" + w + "x" + h + ".png");
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }

            throw e;
        }
    }

    private byte [] getBrandingIcon(String rootEndpoint, int size) {
        try {
            return imageRestService.getImage(rootEndpoint + "/branding/icon/" + size + "x" + size + ".png");
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }

            throw e;
        }
    }
}
