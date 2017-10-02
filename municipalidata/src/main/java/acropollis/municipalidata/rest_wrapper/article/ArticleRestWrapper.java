package acropollis.municipalidata.rest_wrapper.article;

import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dto.article.Article;
import acropollis.municipalidata.rest.omega.article.ArticleRest;
import acropollis.municipalidata.service.article.ArticleImageService;
import acropollis.municipalidata.service.article.ArticleService;
import acropollis.municipalidata.service.configuration.ConfigurationService;
import acropollis.municipalidata.utils.ScreenUtils;

@EBean
public class ArticleRestWrapper {
    @RestService
    ArticleRest articleRest;

    @Bean
    ArticleService articleService;
    @Bean
    ArticleImageService articleImageService;
    @Bean
    ConfigurationService configurationService;

    @Bean
    ScreenUtils screenUtils;

    public RestResult<List<Article>> loadArticles(ProductConfiguration configuration) {
        try {
            String serverRootUrl = configurationService
                    .getServerRootUrl(configuration).orElseThrow(RuntimeException::new);

            List<Article> articles = articleRest.getArticles(serverRootUrl);

            articleService.setArticles(configuration, articles);

            return RestResult.success(articles);
        } catch (Exception e) {
            return RestResult.failure();
        }
    }

    public RestResult<byte []> loadArticleImage(ProductConfiguration configuration, long articleId) {
        try {
            String imageHostingRootUrl = configurationService
                    .getImageHostingRootUrl(configuration).orElseThrow(RuntimeException::new);

            int imageSize = getArticleImageSize();

            byte [] image = articleRest.getArticleImage(imageHostingRootUrl, articleId, imageSize);

            articleImageService.saveArticleImage(configuration, articleId, image);

            return RestResult.success(image);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                articleImageService.saveArticleImage(configuration, articleId, new byte [0]);

                return RestResult.success(null);
            }

            return RestResult.failure();
        } catch (Exception e) {
            return RestResult.failure();
        }
    }

    private int getArticleImageSize() {
        switch (screenUtils.getScreenDensity()) {
            case LDPI:
                return 150;
            case MDPI:
                return 225;
            case HDPI:
                return 300;
            case XHDPI:
                return 450;
            case XXHDPI:
                return 600;
            case XXXHDPI:
                return 600;
            default: {
                throw new RuntimeException();
            }
        }
    }
}
