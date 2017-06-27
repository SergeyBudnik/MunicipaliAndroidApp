package acropollis.municipali.rest.wrappers.alpha;

import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.List;

import acropollis.municipali.data.backend.Product;
import acropollis.municipali.data.backend.ProductComponent;
import acropollis.municipali.data.backend.ProductTier;
import acropollis.municipali.rest.raw.alpha.AlphaProductRestService;
import acropollis.municipali.rest.wrappers.RestListener;

@EBean
public class AlphaProductRestWrapper {
    @RestService
    AlphaProductRestService alphaProductRestService;

    @Background
    public void getDemoProducts(RestListener<List<Product>> products) {
        try {
            products.onStart();

            List<Product> allProducts = alphaProductRestService.getAllProducts();

            List<Product> filteredProducts = new ArrayList<>();

            for (Product product : allProducts) {
                if (product.getTier() == ProductTier.DEMO && product.getComponent() == ProductComponent.USER) {
                    filteredProducts.add(product);
                }
            }

            products.onSuccess(filteredProducts);
        } catch (Exception e) {
            Log.e("AlphaProductRestWrapper", "Get demo products failed", e);

            products.onFailure();
        }
    }
}
