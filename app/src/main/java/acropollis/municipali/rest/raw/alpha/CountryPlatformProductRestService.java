package acropollis.municipali.rest.raw.alpha;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import acropollis.municipali.data.backend.CountryPlatformFullBackendInfo;
import acropollis.municipali.data.country.Country;

@Rest(
    rootUrl = AlphaRestConfiguration.ALPHA_BASE_URL,
    converters = MappingJacksonHttpMessageConverter.class
)
public interface CountryPlatformProductRestService {
    @Get("/user/country-platform/product/{productId}/country")
    Country getCountry(String productId);
    @Get("/user/country-platform/product/{productId}/city/{cityId}")
    CountryPlatformFullBackendInfo getBackendInfo(String productId, long cityId);
}
