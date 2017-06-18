package acropollis.municipali.rest.raw.alpha;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

@Rest(
    rootUrl = AlphaRestConfiguration.ALPHA_BASE_URL,
    converters = ByteArrayHttpMessageConverter.class
)
public interface CitiesIconsRestService {
    @Get("/user/country/{countryId}/city/{cityId}/icon/{iconSize}")
    byte [] getCityIcon(long countryId, long cityId, int iconSize);
}
