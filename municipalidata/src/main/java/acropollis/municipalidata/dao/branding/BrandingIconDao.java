package acropollis.municipalidata.dao.branding;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.common.CommonSingletonImageDao;

@EBean(scope = EBean.Scope.Singleton)
public class BrandingIconDao extends CommonSingletonImageDao {
    @RootContext
    Context context;

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected String getFileName(ProductConfiguration configuration) {
        return BrandingIconDao.class.getCanonicalName() + "." + configuration.getProductId();
    }

    @Override
    protected void logError(String message) {
        Log.e(BrandingIconDao.class.getSimpleName(), message);
    }
}
