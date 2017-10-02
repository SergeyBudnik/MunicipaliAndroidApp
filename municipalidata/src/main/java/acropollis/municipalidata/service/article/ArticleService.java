package acropollis.municipalidata.service.article;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.article.ArticleAnswerIconDao;
import acropollis.municipalidata.dao.article.ArticleDao;
import acropollis.municipalidata.dao.article.ArticleImageDao;
import acropollis.municipalidata.dto.article.Article;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.Question;
import acropollis.municipalidata.dto.article.question.answer.Answer;

@EBean
public class ArticleService {
    @Bean
    ArticleDao articlesDao;
    @Bean
    ArticleImageDao articleImageDao;
    @Bean
    ArticleAnswerIconDao articleAnswerIconDao;

    public Collection<TranslatedArticle> getArticles(ProductConfiguration configuration) {
        return Stream
                .of(new ArrayList<>(articlesDao.getArticles(configuration).values()))
                .filter(it -> it.isTranslateable(configuration.getLanguage()))
                .map(it -> it.translate(configuration.getLanguage()))
                .toList();
    }

    public Optional<TranslatedArticle> getArticle(ProductConfiguration configuration, long id) {
        return articlesDao
                .getArticle(configuration, id)
                .filter(it -> it.isTranslateable(configuration.getLanguage()))
                .map(it -> it.translate(configuration.getLanguage()));
    }

    public void setArticles(ProductConfiguration configuration, Collection<Article> newArticles) {
        Map<Long, Article> existingArticlesMap = articlesDao.getArticles(configuration);
        Map<Long, Article> newArticlesMap = Stream.of(newArticles).collect(Collectors.toMap(Article::getId, it -> it));

        /* Remove gone articles icons */
        Stream.of(existingArticlesMap).forEach(existingArticleMapEntry -> {
            if (!newArticlesMap.containsKey(existingArticleMapEntry.getKey())) {
                clearIcons(configuration, existingArticleMapEntry.getValue());
            }
        });

        /* Remove outdated articles icons */
        Stream.of(newArticlesMap).forEach(newArticlesMapEntry -> {
            Article newArticle = newArticlesMapEntry.getValue();
            Article existingArticle = existingArticlesMap.get(newArticlesMapEntry.getKey());

            if (existingArticle != null && existingArticle.getLastUpdateDate() < newArticle.getLastUpdateDate()) {
                clearIcons(configuration, existingArticle);
            }
        });

        articlesDao.setArticles(configuration, newArticlesMap);
    }

    private void clearIcons(ProductConfiguration configuration, Article article) {
        articleImageDao.removeIcon(configuration, article.getId());

        for (Question question : article.getQuestions()) {
            for (Answer answer : question.getAnswers()) {
                articleAnswerIconDao.removeIcon(configuration, answer.getId());
            }
        }
    }
}
