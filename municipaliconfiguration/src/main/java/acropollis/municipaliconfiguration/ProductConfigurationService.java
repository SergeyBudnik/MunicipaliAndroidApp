package acropollis.municipaliconfiguration;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class ProductConfigurationService {
    @RootContext
    Context context;

    public String getProductConfiguration() {
        return context.getResources().getString(R.string.product_id);
    }
}
