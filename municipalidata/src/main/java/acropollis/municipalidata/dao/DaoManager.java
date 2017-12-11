package acropollis.municipalidata.dao;

import android.content.Context;

import com.annimon.stream.Stream;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.answer.UserAnswersDao;
import acropollis.municipalidata.dao.article.ArticleAnswerIconDao;
import acropollis.municipalidata.dao.article.ArticleClippedImageDao;
import acropollis.municipalidata.dao.article.ArticleDao;
import acropollis.municipalidata.dao.article.ArticleImageDao;
import acropollis.municipalidata.dao.configuration.ConfigurationDao;

@EBean
public class DaoManager {
    @Bean
    ArticleDao articleDao;
    @Bean
    ConfigurationDao configurationDao;
    @Bean
    UserAnswersDao userAnswersDao;
    @Bean
    ArticleImageDao articleImageDao;
    @Bean
    ArticleClippedImageDao articleClippedImageDao;
    @Bean
    ArticleAnswerIconDao articleAnswerIconDao;

    @RootContext
    Context context;

    public void clearAll(ProductConfiguration productConfiguration) {
        Stream.of(articleDao.getArticles(productConfiguration).values()).forEach(article -> {
            articleImageDao.removeIcon(productConfiguration, article.getId());
            articleClippedImageDao.removeIcon(productConfiguration, article.getId());

            Stream.of(article.getQuestions()).forEach(question -> {
                Stream.of(question.getAnswers()).forEach(answer -> {
                    articleAnswerIconDao.removeIcon(productConfiguration, answer.getId());
                });
            });
        });

        articleDao.clear(productConfiguration, context);
        configurationDao.clear(productConfiguration, context);
        userAnswersDao.clear(productConfiguration, context);
    }
}
