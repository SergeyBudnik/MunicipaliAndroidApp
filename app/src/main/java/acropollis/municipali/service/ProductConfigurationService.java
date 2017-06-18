package acropollis.municipali.service;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipali.R;
import acropollis.municipali.configuration.ProductConfiguration;

@EBean
public class ProductConfigurationService {
    @RootContext
    Context context;

    public ProductConfiguration getProductConfiguration() {
        return ProductConfiguration
                .getProductConfiguration(context.getResources().getString(R.string.product_id));
    }
}
