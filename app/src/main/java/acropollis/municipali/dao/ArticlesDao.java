package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.data.article.Article;
import acropollis.municipali.data.article.TranslatedArticle;
import acropollis.municipali.data.common.Language;
import acropollis.municipali.service.ProductConfigurationService;
import lombok.Data;

@Data
class ArticlesData {
    private Map<Long, Article> allArticles = new HashMap<>();
    private Map<Language, Map<Long, TranslatedArticle>> allTranslatedArticles = new HashMap<>();

    ArticlesData() {
        for (Language language : Language.values()) {
            allTranslatedArticles.put(language, new HashMap<Long, TranslatedArticle>());
        }
    }
}

@EBean(scope = EBean.Scope.Singleton)
public class ArticlesDao extends CommonDao<ArticlesData> {
    @RootContext
    Context context;

    @Bean
    ProductConfigurationService productConfigurationService;

    public Map<Long, Article> getArticles() {
        readCache(context, getFileName(), false);

        return getValue().getAllArticles();
    }

    public Map<Long, TranslatedArticle> getTranslatedArticles(Language language) {
        readCache(context, getFileName(), false);

        return getValue().getAllTranslatedArticles().get(language);
    }

    public TranslatedArticle getTranslatedArticle(Language language, long id) {
        readCache(context, getFileName(), false);

        return getValue().getAllTranslatedArticles().get(language).get(id);
    }

    public void setArticles(
            Map<Long, Article> articles,
            Map<Language, Map<Long, TranslatedArticle>> translatedArticles
    ) {
        readCache(context, getFileName(), false);

        getValue().setAllArticles(articles);
        getValue().setAllTranslatedArticles(translatedArticles);

        persist(context);
    }

    @Override
    protected ArticlesData newInstance() {
        return new ArticlesData();
    }

    @Override
    protected String getFileName() {
        return ArticlesDao.class.getCanonicalName() +
                "." +
                productConfigurationService.getProductConfiguration().getProductId();
    }
}
