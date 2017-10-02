package acropollis.municipalidata.rest_wrapper.answer;

import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Locale;
import java.util.Map;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.rest.omega.answer.AnswerRequest;
import acropollis.municipalidata.rest.omega.answer.AnswerRest;
import acropollis.municipalidata.rest_wrapper.article.RestResult;
import acropollis.municipalidata.service.configuration.ConfigurationService;
import acropollis.municipalidata.utils.ScreenUtils;

@EBean
public class AnswerRestWrapper {
    @RestService
    AnswerRest answerRest;

    @Bean
    ConfigurationService configurationService;

    @Bean
    ScreenUtils screenUtils;

    public RestResult<Void> answerQuestion(
            ProductConfiguration configuration,
            String userId, long articleId, long questionId, long answerId
    ) {
        try {
            String serverRootUrl = configurationService
                    .getServerRootUrl(configuration)
                    .orElseThrow(RuntimeException::new);

            AnswerRequest request = new AnswerRequest(); {
                request.setUserAuthToken(userId);
                request.setArticleId(articleId);
                request.setQuestionId(questionId);
                request.setAnswerId(answerId);
            }

            answerRest.answerQuestion(serverRootUrl, request);

            return RestResult.success(null);
        } catch (Exception e) {
            return RestResult.failure();
        }
    }

    public RestResult<Map<Long, Long>> getAnswer(
            ProductConfiguration configuration,
            long articleId, long questionId
    ) {
        try {
            String serverRootUrl = configurationService
                    .getServerRootUrl(configuration)
                    .orElseThrow(RuntimeException::new);

            Map<Long, Long> response = answerRest.getAnswerStatistics(
                    serverRootUrl, articleId, questionId
            );

            return RestResult.success(response);
        } catch (Exception e) {
            return RestResult.failure();
        }
    }

    public RestResult<byte []> loadAnswerIcon(ProductConfiguration configuration, long answerId) {
        try {
            String serverRootUrl = configurationService
                    .getServerRootUrl(configuration)
                    .orElseThrow(RuntimeException::new);

            byte [] icon = answerRest.getAnswerIcon(serverRootUrl, answerId, getIconSize());

            return RestResult.success(icon);
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return RestResult.success(null);
            }

            return RestResult.failure();
        } catch (Exception e) {
            return RestResult.failure();
        }
    }

    public int getIconSize() {
        ScreenUtils.Density screenDensity = screenUtils.getScreenDensity();

        switch (screenDensity) {
            case LDPI:
                return 50;
            case MDPI:
                return 50;
            case HDPI:
                return 75;
            case XHDPI:
                return 100;
            case XXHDPI:
                return 150;
            case XXXHDPI:
                return 200;
            default: {
                throw new RuntimeException();
            }
        }
    }
}
