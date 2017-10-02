package acropollis.municipalidata.dao.article;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.common.CommonImageDao;

@EBean(scope = EBean.Scope.Singleton)
public class ArticleAnswerIconDao extends CommonImageDao<Long> {
    @RootContext
    Context context;

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected String getFileName(ProductConfiguration configuration, Long id) {
        return ArticleAnswerIconDao.class.getCanonicalName() + "." + configuration.getProductId() + "." + id;
    }

    @Override
    protected void logError(String message) {
        Log.e(ArticleAnswerIconDao.class.getSimpleName(), message);
    }
}
