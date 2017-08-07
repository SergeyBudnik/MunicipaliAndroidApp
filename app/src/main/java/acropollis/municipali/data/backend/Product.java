package acropollis.municipali.data.backend;

import java.io.Serializable;

import lombok.Data;

@Data
public class Product implements Serializable {
    private String id;
    private String groupId;
    private ProductTier tier;
    private ProductComponent component;
    private ProductType type;
    private String endpoint;
}
