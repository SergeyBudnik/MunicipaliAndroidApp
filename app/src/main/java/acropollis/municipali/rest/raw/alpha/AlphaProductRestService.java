package acropollis.municipali.rest.raw.alpha;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.util.List;

import acropollis.municipali.data.backend.Product;

@Rest(
    rootUrl = AlphaRestConfiguration.ALPHA_BASE_URL,
    converters = MappingJacksonHttpMessageConverter.class
)
public interface AlphaProductRestService {
    @Get("/product")
    List<Product> getAllProducts();
    @Get("/product/{productId}")
    Product getProduct(String productId);
}
