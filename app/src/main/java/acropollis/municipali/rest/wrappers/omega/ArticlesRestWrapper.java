package acropollis.municipali.rest.wrappers.omega;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.Map;

import acropollis.municipali.data.article.Article;
import acropollis.municipali.data.rest.request.AnswerRequest;
import acropollis.municipali.rest.raw.common.ImageRestService;
import acropollis.municipali.rest.raw.omega.ArticlesRestService;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.ArticlesService;
import acropollis.municipali.service.BackendInfoService;

@EBean
public class ArticlesRestWrapper {
    @RestService
    ArticlesRestService articlesRestService;
    @RestService
    ImageRestService imageRestService;

    @Bean
    BackendInfoService backendInfoService;
    @Bean
    ArticlesService articlesService;

    @Background
    public void loadArticles(RestListener<Void> listener) {
        try {
            listener.onStart();

            List<Article> articles = articlesRestService.getArticles(
                    backendInfoService.getBackendInfo().getRootEndpoint()
            );

            articlesService.setArticles(articles);

            listener.onSuccess(null);
        } catch (Exception e) {
            listener.onFailure();
        }
    }

    @Background
    public void answerQuestion(String userId, long articleId, long questionId, long answerId, RestListener<Void> listener) {
        try {
            listener.onStart();

            AnswerRequest request = new AnswerRequest(); {
                request.setUserAuthToken(userId);
                request.setArticleId(articleId);
                request.setQuestionId(questionId);
                request.setAnswerId(answerId);
            }

            articlesRestService.answerQuestion(
                    backendInfoService.getBackendInfo().getRootEndpoint(),
                    request
            );

            listener.onSuccess(null);
        } catch (Exception e) {
            listener.onFailure();
        }
    }

    @Background
    public void getAnswer(long articleId, long questionId, RestListener<Map<Long, Long>> listener) {
        try {
            listener.onStart();

            Map<Long, Long> response = articlesRestService.getAnswerStatistics(
                    backendInfoService.getBackendInfo().getRootEndpoint(),
                    articleId,
                    questionId
            );

            listener.onSuccess(response);
        } catch (Exception e) {
            listener.onFailure();
        }
    }

    @Background
    public void loadArticleIcon(long articleId, RestListener<byte []> listener) {
        try {
            listener.onStart();

            byte [] icon = imageRestService.getImage(
                    backendInfoService.getBackendInfo().getRootEndpoint() +
                            "/article/" + articleId + "/icon/" + 300 // ToDo: size
            );

            listener.onSuccess(icon);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                listener.onSuccess(new byte [0]);
            }

            listener.onFailure();
        } catch (Exception e) {
            listener.onFailure();
        }
    }

    @Background
    public void loadAnswerIcon(long articleId, long questionId, long answerid, RestListener<byte []> listener) {
        try {
            listener.onStart();

            byte [] icon = imageRestService.getImage(
                    backendInfoService.getBackendInfo().getRootEndpoint() +
                            "/article/" + articleId + "/question/" + questionId + "/answer/" + answerid + "/icon/" + 300 // ToDo: size
            );

            listener.onSuccess(icon);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                listener.onSuccess(new byte [0]);
            }

            listener.onFailure();
        } catch (Exception e) {
            listener.onFailure();
        }
    }
}
