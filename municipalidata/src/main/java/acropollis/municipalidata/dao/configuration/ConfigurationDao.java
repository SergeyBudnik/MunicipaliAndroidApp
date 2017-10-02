package acropollis.municipalidata.dao.configuration;

import android.content.Context;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.common.CommonDao;

@EBean(scope = EBean.Scope.Singleton)
public class ConfigurationDao extends CommonDao<ConfigurationModel> {
    @RootContext
    Context context;

    public Optional<String> getImageHostingRootUrl(ProductConfiguration configuration) {
        readCache(context, getFileName(configuration), false);

        return Optional.ofNullable(getValue().getImageHostingRootUrl());
    }

    public void setImageHostingRootUrl(ProductConfiguration configuration, String imageHostingRootUrl) {
        readCache(context, getFileName(configuration), false);

        getValue().setImageHostingRootUrl(imageHostingRootUrl);

        persist(configuration, context);
    }

    public Optional<String> getServerRootUrl(ProductConfiguration configuration) {
        readCache(context, getFileName(configuration), false);

        return Optional.ofNullable(getValue().getServerRootUrl());
    }

    public void setServerRootUrl(ProductConfiguration configuration, String serverRootUrl) {
        readCache(context, getFileName(configuration), false);

        getValue().setServerRootUrl(serverRootUrl);

        persist(configuration, context);
    }

    @Override
    protected ConfigurationModel newInstance() {
        return new ConfigurationModel();
    }

    @Override
    protected String getFileName(ProductConfiguration configuration) {
        return ConfigurationDao.class.getCanonicalName() + "." + configuration.getProductId();
    }
}
