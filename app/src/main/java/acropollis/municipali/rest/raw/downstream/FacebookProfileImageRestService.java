package acropollis.municipali.rest.raw.downstream;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

@Rest(rootUrl = "https://graph.facebook.com/", converters = {ByteArrayHttpMessageConverter.class})
public interface FacebookProfileImageRestService {
    @Get("{userId}/picture?type=large")
    byte [] getImage(String userId);
}
