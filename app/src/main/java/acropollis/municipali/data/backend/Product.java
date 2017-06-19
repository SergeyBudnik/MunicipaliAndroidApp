package acropollis.municipali.data.backend;

import lombok.Data;

@Data
public class Product {
    private String id;
    private ProductTier tier;
    private ProductComponent component;
    private ProductType type;
    private String endpoint;
}
