package acropollis.municipalidata.dao.article;

import android.content.Context;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Map;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.common.CommonDao;
import acropollis.municipalidata.dto.article.Article;

@EBean(scope = EBean.Scope.Singleton)
public class ArticleDao extends CommonDao<ArticleModel> {
    @RootContext
    Context context;

    public Map<Long, Article> getArticles(ProductConfiguration configuration) {
        readCache(context, getFileName(configuration), false);

        return getValue().getAllArticles();
    }

    public Optional<Article> getArticle(ProductConfiguration configuration, long id) {
        readCache(context, getFileName(configuration), false);

        return Optional.ofNullable(getValue().getAllArticles().get(id));
    }

    public void setArticles(ProductConfiguration configuration, Map<Long, Article> articles) {
        readCache(context, getFileName(configuration), false);

        getValue().setAllArticles(articles);

        persist(configuration, context);
    }

    @Override
    protected ArticleModel newInstance() {
        return new ArticleModel();
    }

    @Override
    protected String getFileName(ProductConfiguration configuration) {
        return ArticleDao.class.getCanonicalName() + "." + configuration.getProductId();
    }
}
