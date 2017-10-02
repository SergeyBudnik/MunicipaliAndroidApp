package acropollis.municipalidata.rest.omega.answer;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.util.Map;

@Rest(converters = {MappingJacksonHttpMessageConverter.class, ByteArrayHttpMessageConverter.class})
public interface AnswerRest {
    @Post("{rootEndpoint}/answer")
    void answerQuestion(String rootEndpoint, AnswerRequest request);

    @Get("{rootEndpoint}/answer/{articleId}/{questionId}")
    Map<Long, Long> getAnswerStatistics(String rootEndpoint, long articleId, long questionId);

    @Get("{rootEndpoint}/answers/icons/{answerId}/{size}x{size}.png")
    byte [] getAnswerIcon(String rootEndpoint, long answerId, int size);
}
