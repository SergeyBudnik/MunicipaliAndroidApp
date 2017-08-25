package acropollis.municipali.rest.raw.omega;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import acropollis.municipali.data.backend.ImageHostingInfo;

@Rest(converters = MappingJacksonHttpMessageConverter.class)
public interface ConfigurationRestService {
    @Get("{rootEndpoint}/configuration/image-hosting")
    ImageHostingInfo getImageHostingInfo(String rootEndpoint);
}
