package acropollis.municipali.rest.raw;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import acropollis.municipali.data.AnswerRequest;
import acropollis.municipali.data.QuestionStatistics;

@Rest(rootUrl = RestConfiguration.BASE_URL + "/user/", converters = { MappingJacksonHttpMessageConverter.class })
public interface UserAnswerRestService {
    @Post("answer")
    void answer(AnswerRequest answerRequest);

    @Get("answer/question/{questionId}")
    QuestionStatistics getStatistics(long questionId);
}
