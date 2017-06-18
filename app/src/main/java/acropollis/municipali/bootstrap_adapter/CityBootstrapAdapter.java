package acropollis.municipali.bootstrap_adapter;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import acropollis.municipali.R;
import acropollis.municipali.bootstrap.data.MunicipaliLoadableIconData;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.configuration.ProductConfiguration;
import acropollis.municipali.data.city.City;
import acropollis.municipali.data.common.Language;
import acropollis.municipali.data.country.Country;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.alpha.CountryPlatformProductRestWrapper;

@EBean
public class CityBootstrapAdapter {
    @RootContext
    Context context;

    @Bean
    CountryPlatformProductRestWrapper countryPlatformProductRestWrapper;

    public List<MunicipaliRowData> getCitiesRows(Country country) {
        List<MunicipaliRowData> municipaliRowDataList = new ArrayList<>();

        Language language = getCurrentLanguage();

        for (City city : country.getCities()) {
            municipaliRowDataList.add(getQuestionRowInfo(language, country, city));
        }

        return municipaliRowDataList;
    }

    private MunicipaliRowData getQuestionRowInfo(Language language, Country country, City city) {
        return new MunicipaliRowData(
                city.getId(),
                city.getName().get(language),
                new MunicipaliLoadableIconData(
                        city.getId(),
                        null,
                        getCityIconLoader(country, city)
                ),
                MunicipaliRowData.NO_TRANSPARENCY,
                MunicipaliRowData.NO_COUNTER
        );
    }

    private MunicipaliLoadableIconData.IconFromNetworkLoader getCityIconLoader(final Country country, final City city) {
        return new MunicipaliLoadableIconData.IconFromNetworkLoader() {
            @Override
            public void load(final MunicipaliLoadableIconData.IconLoadingListener listener) {
                countryPlatformProductRestWrapper.getCityIcon(country, city,
                        new RestListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] icon) {
                                listener.onSuccess(icon);
                            }

                            @Override
                            public void onFailure() {
                                listener.onFailure();
                            }
                        });
            }
        };
    }

    private Language getCurrentLanguage() {
        return ProductConfiguration
                .getProductConfiguration(
                        context
                                .getResources()
                                .getString(R.string.product_id)
                ).getLanguage();
    }
}
