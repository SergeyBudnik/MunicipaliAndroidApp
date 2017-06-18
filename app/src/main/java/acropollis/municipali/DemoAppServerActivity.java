package acropollis.municipali;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import acropollis.municipali.bootstrap.view.MunicipaliListView;
import acropollis.municipali.bootstrap_adapter.DemoAppServersAdapter;
import acropollis.municipali.data.backend.Product;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.alpha.AlphaProductRestWrapper;

@EActivity(R.layout.activity_demo_app_server)
public class DemoAppServerActivity extends BaseActivity {
    @ViewById(R.id.servers_list)
    MunicipaliListView serversListView;

    @Bean
    AlphaProductRestWrapper alphaProductRestWrapper;

    @Bean
    DemoAppServersAdapter demoAppServersAdapter;

    private String selectedProductId = null;

    @AfterViews
    void init() {
        alphaProductRestWrapper.getProducts(new RestListener<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                onProductsLoaded(products);
            }
        });
    }

    @Click(R.id.demo_ok)
    void onOkClick() {
        redirect(StandaloneCustomerLoadingActivity_.class, 0, 0, true,
                Collections.<String, Serializable>singletonMap("demoProductId", selectedProductId)
        );
    }

    @UiThread
    void onProductsLoaded(List<Product> products) {
        serversListView.setElements(demoAppServersAdapter.getProducts(products));

        selectedProductId = products.get(0).getId();
        serversListView.setItemSelected(0);
    }

    @ItemClick(R.id.list)
    void onProductClick(int position) {
        selectedProductId = serversListView.getElement(position).getText();
        serversListView.setItemSelected(position);
    }
}
