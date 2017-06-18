package acropollis.municipali.service;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.R;
import acropollis.municipali.configuration.ProductConfiguration;
import acropollis.municipali.dao.AnswersIconDao;
import acropollis.municipali.dao.ArticlesDao;
import acropollis.municipali.dao.ArticlesIconDao;
import acropollis.municipali.data.article.Article;
import acropollis.municipali.data.article.TranslatedArticle;
import acropollis.municipali.data.article.question.Question;
import acropollis.municipali.data.article.question.answer.Answer;
import acropollis.municipali.data.common.Language;

@EBean
public class ArticlesService {
    @RootContext
    Context context;

    @Bean
    ArticlesDao articlesDao;
    @Bean
    ArticlesIconDao articlesIconDao;
    @Bean
    AnswersIconDao answersIconDao;

    public Collection<TranslatedArticle> getArticles() {
        return articlesDao.getTranslatedArticles(getCurrentLanguage()).values();
    }

    public TranslatedArticle getArticle(long id) {
        return articlesDao.getTranslatedArticle(getCurrentLanguage(), id);
    }

    public void setArticles(Collection<Article> newArticles) {
        Map<Long, Article> existingArticlesMap = articlesDao.getArticles();
        Map<Long, Article> newArticlesMap = toMap(newArticles);

        /* Remove gone articles icons */
        for (long existingArticleId : existingArticlesMap.keySet()) {
            if (!newArticlesMap.containsKey(existingArticleId)) {
                Article existingArticle = existingArticlesMap.get(existingArticleId);

                clearIcons(existingArticle);
            }
        }

        /* Remove outdated articles icons */
        for (long newArticleId : newArticlesMap.keySet()) {
            Article newArticle = newArticlesMap.get(newArticleId);
            Article existingArticle = existingArticlesMap.get(newArticleId);

            if (existingArticle != null && existingArticle.getLastUpdateDate() < newArticle.getLastUpdateDate()) {
                clearIcons(existingArticle);
            }
        }

        articlesDao.setArticles(newArticlesMap, getTranslatedArticles(newArticles));
    }

    private void clearIcons(Article article) {
        articlesIconDao.removeIcon(article.getId());

        for (Question question : article.getQuestions()) {
            for (Answer answer : question.getAnswers()) {
                answersIconDao.removeIcon(answer.getId());
            }
        }
    }

    public byte [] getArticleIcon(long id) {
        return articlesIconDao.getIcon(id);
    }

    public void saveArticleIcon(long id, byte [] icon) {
        articlesIconDao.addIcon(id, icon);
    }

    public byte [] getAnswerIcon(long id) {
        return answersIconDao.getIcon(id);
    }

    public void saveAnswerIcon(long id, byte [] icon) {
        answersIconDao.addIcon(id, icon);
    }

    private Map<Long, Article> toMap(Collection<Article> articles) {
        Map<Long, Article> articleMap = new HashMap<>();

        for (Article article : articles) {
            articleMap.put(article.getId(), article);
        }

        return articleMap;
    }

    private Map<Language, Map<Long, TranslatedArticle>> getTranslatedArticles(Collection<Article> articles) {
        Map<Language, Map<Long, TranslatedArticle>> translatedArticles = new HashMap<>();

        for (Language language : Language.values()) {
            translatedArticles.put(language, new HashMap<Long, TranslatedArticle>());

            for (Article article : articles) {
                if (article.isTranslateable(language)) {
                    translatedArticles.get(language).put(article.getId(), article.translate(language));
                }
            }
        }

        return translatedArticles;
    }

    private Language getCurrentLanguage() {
        return ProductConfiguration
                .getProductConfiguration(
                        context
                                .getResources()
                                .getString(R.string.product_id)
                ).getLanguage();
    }
}
