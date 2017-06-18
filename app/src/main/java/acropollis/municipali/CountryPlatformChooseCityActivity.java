package acropollis.municipali;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.Collections;

import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.bootstrap.view.MunicipaliRefreshableListView;
import acropollis.municipali.bootstrap_adapter.CityBootstrapAdapter;
import acropollis.municipali.configuration.ProductConfiguration;
import acropollis.municipali.data.country.Country;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.alpha.CountryPlatformProductRestWrapper;

@EActivity(R.layout.activity_choose_city)
public class CountryPlatformChooseCityActivity extends BaseActivity {
    @ViewById(R.id.cities_list)
    MunicipaliRefreshableListView citiesListView;

    @Bean
    CountryPlatformProductRestWrapper countryPlatformProductRestWrapper;

    @Bean
    CityBootstrapAdapter cityBootstrapAdapter;

    @AfterViews
    void init() {
        citiesListView.init(getCitiesLoader());
    }

    @ItemClick(R.id.list)
    void onCityClick(MunicipaliRowData cityRow) {
        redirect(CountryPlatformCustomerLoadingActivity_.class, 0, 0, true,
                Collections.singletonMap("cityId", (Serializable) cityRow.getId()));
    }

    private MunicipaliRefreshableListView.RowsLoader getCitiesLoader() {
        final ProductConfiguration currentProductConfiguration = ProductConfiguration
                .getProductConfiguration(getResources().getString(R.string.product_id));

        return new MunicipaliRefreshableListView.RowsLoader() {
            @Override
            public void load(final MunicipaliRefreshableListView.LoadingListener listener) {
                countryPlatformProductRestWrapper.getCountry(
                        currentProductConfiguration.getProductId(),
                        new RestListener<Country>() {
                            @Override
                            public void onSuccess(Country country) {
                                listener.onSuccess(cityBootstrapAdapter.getCitiesRows(country));
                            }

                            @Override
                            public void onFailure() {
                                listener.onFailure();

                                onLoadingFailed();
                            }
                        });
            }
        };
    }

    @UiThread
    void onLoadingFailed() {
    }
}
