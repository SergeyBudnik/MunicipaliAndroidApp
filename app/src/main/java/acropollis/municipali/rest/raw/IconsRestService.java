package acropollis.municipali.rest.raw;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;

import acropollis.municipali.data.ImageSize;

@Rest(rootUrl = RestConfiguration.BASE_URL + "/user/", converters = {ByteArrayHttpMessageConverter.class})
public interface IconsRestService {
    @Get("questions/{questionId}/icon/{iconSize}")
    byte [] getQuestionIcon(long questionId, ImageSize iconSize);

    @Get("questions/{questionId}/answer/{answerId}/icon/{iconSize}")
    byte [] getAnswerIcon(long questionId, long answerId, ImageSize iconSize);
}
