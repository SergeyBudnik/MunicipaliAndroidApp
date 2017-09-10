package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.Serializable;

import acropollis.municipalidata.dto.common.Language;
import acropollis.municipali.service.ProductConfigurationService;
import lombok.Data;

@Data
class AppSettingsData implements Serializable {
    private Language language = Language.ENGLISH;
}

@EBean(scope = EBean.Scope.Singleton)
public class AppSettingsDao extends CommonDao<AppSettingsData> {
    @RootContext
    Context context;

    @Bean
    ProductConfigurationService productConfigurationService;

    public Language getLanguage() {
        readCache(context, getFileName(), false);

        return getValue().getLanguage();
    }

    public void setLanguage(Language language) {
        readCache(context, getFileName(), false);

        getValue().setLanguage(language);

        persist(context);
    }

    @Override
    protected AppSettingsData newInstance() {
        return new AppSettingsData();
    }

    @Override
    protected String getFileName() {
        return AppSettingsDao.class.getCanonicalName() +
                "." +
                productConfigurationService.getProductConfiguration().getProductId();
    }
}
