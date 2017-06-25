package acropollis.municipali.configuration;

import acropollis.municipali.data.common.Language;
import lombok.Getter;

public enum ProductConfiguration {
    MUNICIPALI_QA("MunicipaliQA-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.ENGLISH),
    MUNICIPALI_DEMO("MunicipaliDemo-User", ProductTier.DEMO, ProductType.MUNICIPALI, Language.ENGLISH, Language.ENGLISH),
    MUNICIPALI_KARPOS("MunicipaliProd-Karpos-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.MACEDONIAN),
    MUNICIPALI_ARAD("MunicipaliProd-Arad-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.HEBREW),

    DEMOCRACY_QA("DemocracyQA-User", ProductTier.STANDALONE, ProductType.DEMOCRACY, Language.ENGLISH, Language.ENGLISH);

    @Getter private String productId;
    @Getter private ProductTier productTier;
    @Getter private ProductType productType;
    @Getter private Language language;
    @Getter private Language uiLanguage;

    ProductConfiguration(
            String productId,
            ProductTier productTier,
            ProductType productType,
            Language language,
            Language uiLanguage
    ) {
        this.productId = productId;
        this.productTier = productTier;
        this.productType = productType;
        this.language = language;
        this.uiLanguage = uiLanguage;
    }

    public static ProductConfiguration getProductConfiguration(String productId) {
        for (ProductConfiguration productConfiguration : ProductConfiguration.values()) {
            if (productConfiguration.getProductId().equals(productId)) {
                return productConfiguration;
            }
        }

        throw new IllegalArgumentException();
    }
}
