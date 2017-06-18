package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipali.data.backend.BackendInfo;
import lombok.Data;

@Data
class BackendInfoData {
    private BackendInfo backendInfo;
}

@EBean(scope = EBean.Scope.Singleton)
public class BackendInfoDao extends CommonDao<BackendInfoData> {
    private static final String FILE_NAME = BackendInfoDao.class.getCanonicalName();

    @RootContext
    Context context;

    public BackendInfo getBackendInfo() {
        readCache(context, getFileName(), false);

        return cache.getBackendInfo();
    }

    public void setBackendInfo(BackendInfo backendInfo) {
        readCache(context, getFileName(), false);

        cache.setBackendInfo(backendInfo);

        persist(context);
    }

    @Override
    protected BackendInfoData newInstance() {
        return new BackendInfoData();
    }

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }
}
