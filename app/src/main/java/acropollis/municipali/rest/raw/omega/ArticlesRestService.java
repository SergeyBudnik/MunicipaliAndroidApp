package acropollis.municipali.rest.raw.omega;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.util.List;
import java.util.Map;

import acropollis.municipali.data.article.Article;
import acropollis.municipali.data.rest.request.AnswerRequest;

@Rest(converters = MappingJacksonHttpMessageConverter.class)
public interface ArticlesRestService {
    @Get("{rootEndpoint}/article")
    List<Article> getArticles(String rootEndpoint);

    @Post("{rootEndpoint}/answer")
    void answerQuestion(String rootEndpoint, AnswerRequest request);

    @Get("{rootEndpoint}/answer/{articleId}/{questionId}")
    Map<Long, Long> getAnswerStatistics(String rootEndpoint, long articleId, long questionId);
}
