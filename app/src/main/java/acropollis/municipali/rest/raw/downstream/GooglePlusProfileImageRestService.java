package acropollis.municipali.rest.raw.downstream;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

@Rest(converters = {ByteArrayHttpMessageConverter.class})
public interface GooglePlusProfileImageRestService {
    @Get("{url}")
    byte [] getImage(String url);
}
