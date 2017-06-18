package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.Serializable;

import acropollis.municipali.data.common.Language;
import lombok.Data;

@Data
class AppSettingsData implements Serializable {
    private Language language = Language.ENGLISH;
}

@EBean(scope = EBean.Scope.Singleton)
public class AppSettingsDao extends CommonDao<AppSettingsData> {
    private static final String FILE_NAME = AppSettingsDao.class.getCanonicalName();

    @RootContext
    Context context;

    public Language getLanguage() {
        readCache(context, FILE_NAME, false);

        return cache.getLanguage();
    }

    public void setLanguage(Language language) {
        readCache(context, FILE_NAME, false);

        cache.setLanguage(language);

        persist(context);
    }

    @Override
    protected AppSettingsData newInstance() {
        return new AppSettingsData();
    }

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }
}
