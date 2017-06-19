package acropollis.municipali.dao;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipali.service.ProductConfigurationService;

@EBean(scope = EBean.Scope.Singleton)
public class AnswersIconDao extends CommonIconDao<Long> {
    @RootContext
    Context context;

    @Bean
    ProductConfigurationService productConfigurationService;

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected String getFileName(Long id) {
        return AnswersIconDao.class.getCanonicalName() +
                "." +
                productConfigurationService.getProductConfiguration().getProductId() +
                "." +
                id;
    }

    @Override
    protected void logError(String message) {
        Log.e(AnswersIconDao.class.getSimpleName(), message);
    }
}
