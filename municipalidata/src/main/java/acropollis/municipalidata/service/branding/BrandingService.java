package acropollis.municipalidata.service.branding;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.branding.BrandingBackgroundDao;
import acropollis.municipalidata.dao.branding.BrandingIconDao;

@EBean
public class BrandingService {
    @Bean
    BrandingBackgroundDao brandingBackgroundDao;
    @Bean
    BrandingIconDao brandingIconDao;

    public void setIcon(ProductConfiguration productConfiguration, byte [] iconBytes) {
        brandingIconDao.setIcon(productConfiguration, iconBytes);
    }

    public Optional<byte []> getIcon(ProductConfiguration productConfiguration) {
        return brandingIconDao.getIcon(productConfiguration);
    }

    public void setBackground(ProductConfiguration productConfiguration, byte [] backgroundBytes) {
        brandingBackgroundDao.setIcon(productConfiguration, backgroundBytes);
    }

    public Optional<byte []> getBackground(ProductConfiguration productConfiguration) {
        return brandingBackgroundDao.getIcon(productConfiguration);
    }
}
