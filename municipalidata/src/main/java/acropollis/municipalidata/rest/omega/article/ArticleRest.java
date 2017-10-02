package acropollis.municipalidata.rest.omega.article;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.util.List;

import acropollis.municipalidata.dto.article.Article;

@Rest(converters = {MappingJacksonHttpMessageConverter.class, ByteArrayHttpMessageConverter.class})
public interface ArticleRest {
    @Get("{rootEndpoint}/article")
    List<Article> getArticles(String rootEndpoint);

    @Get("{rootEndpoint}/articles/images/{articleId}/{size}x{size}.png")
    byte [] getArticleImage(String rootEndpoint, long articleId, int size);
}
