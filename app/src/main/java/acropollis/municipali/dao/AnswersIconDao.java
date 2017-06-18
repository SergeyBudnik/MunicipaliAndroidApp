package acropollis.municipali.dao;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean(scope = EBean.Scope.Singleton)
public class AnswersIconDao extends CommonIconDao<Long> {
    @RootContext
    Context context;

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected String getFileName(Long id) {
        return AnswersIconDao.class.getCanonicalName() + "." + id;
    }

    @Override
    protected void logError(String message) {
        Log.e(AnswersIconDao.class.getSimpleName(), message);
    }
}
