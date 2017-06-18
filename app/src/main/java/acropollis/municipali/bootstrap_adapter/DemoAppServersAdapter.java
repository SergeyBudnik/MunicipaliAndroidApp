package acropollis.municipali.bootstrap_adapter;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.data.backend.Product;
import acropollis.municipali.data.backend.ProductComponent;

@EBean
public class DemoAppServersAdapter {
    public List<MunicipaliRowData> getProducts(List<Product> products) {
        List<MunicipaliRowData> res = new ArrayList<>();

        for (Product product : products) {
            boolean isUser = product.getComponent().equals(ProductComponent.USER);

            if (isUser) {
                res.add(new MunicipaliRowData(
                        0,
                        product.getId(),
                        null,
                        MunicipaliRowData.NO_TRANSPARENCY,
                        MunicipaliRowData.NO_COUNTER
                ));
            }
        }

        return res;
    }
}
