package acropollis.municipali.rest.raw.common;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

@Rest(converters = ByteArrayHttpMessageConverter.class)
public interface ImageRestService {
    @Get("{path}")
    byte [] getImage(String path);
}
