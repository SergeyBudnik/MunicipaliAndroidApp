package acropollis.municipali.rest.raw.alpha;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import acropollis.municipali.data.backend.StandaloneFullBackendInfo;

@Rest(
        rootUrl = AlphaRestConfiguration.ALPHA_BASE_URL,
        converters = MappingJacksonHttpMessageConverter.class
)
public interface StandaloneProductRestService {
    @Get("/user/standalone/product/{productId}")
    StandaloneFullBackendInfo getBackendInfo(String productId);
}
