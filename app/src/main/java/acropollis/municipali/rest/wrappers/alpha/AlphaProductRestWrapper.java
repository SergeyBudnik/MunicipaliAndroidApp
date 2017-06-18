package acropollis.municipali.rest.wrappers.alpha;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import java.util.List;

import acropollis.municipali.data.backend.Product;
import acropollis.municipali.rest.raw.alpha.AlphaProductRestService;
import acropollis.municipali.rest.wrappers.RestListener;

@EBean
public class AlphaProductRestWrapper {
    @RestService
    AlphaProductRestService alphaProductRestService;

    @Background
    public void getProducts(RestListener<List<Product>> products) {
        try {
            products.onStart();

            List<Product> allProducts = alphaProductRestService.getAllProducts();

            products.onSuccess(allProducts);
        } catch (Exception e) {
            products.onFailure();
        }
    }
}
