package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipali.data.backend.BackendInfo;
import acropollis.municipali.service.ProductConfigurationService;
import lombok.Data;

@Data
class BackendInfoData {
    private BackendInfo backendInfo;
}

@EBean(scope = EBean.Scope.Singleton)
public class BackendInfoDao extends CommonDao<BackendInfoData> {
    @RootContext
    Context context;

    @Bean
    ProductConfigurationService productConfigurationService;

    public BackendInfo getBackendInfo() {
        readCache(context, getFileName(), false);

        return getValue().getBackendInfo();
    }

    public void setBackendInfo(BackendInfo backendInfo) {
        readCache(context, getFileName(), false);

        getValue().setBackendInfo(backendInfo);

        persist(context);
    }

    @Override
    protected BackendInfoData newInstance() {
        return new BackendInfoData();
    }

    @Override
    protected String getFileName() {
        return BackendInfoDao.class.getCanonicalName() +
                "." +
                productConfigurationService.getProductConfiguration().getProductId();
    }
}
