package acropollis.municipalidata.service.configuration;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.configuration.ConfigurationDao;

@EBean
public class ConfigurationService {
    @Bean
    ConfigurationDao configurationDao;

    public Optional<String> getImageHostingRootUrl(ProductConfiguration configuration) {
        return configurationDao.getImageHostingRootUrl(configuration);
    }

    public void setImageHostingRootUrl(ProductConfiguration configuration, String imageHostingRootUrl) {
        configurationDao.setImageHostingRootUrl(configuration, imageHostingRootUrl);
    }

    public Optional<String> getServerRootUrl(ProductConfiguration configuration) {
        return configurationDao.getServerRootUrl(configuration);
    }

    public void setServerRootUrl(ProductConfiguration configuration, String serverRootUrl) {
        configurationDao.setServerRootUrl(configuration, serverRootUrl);
    }
}
