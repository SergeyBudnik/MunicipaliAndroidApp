package acropollis.municipali.configuration;

import acropollis.municipalidata.dto.common.Language;
import lombok.Getter;

public enum ProductConfiguration {
    MUNICIPALI_QA("MunicipaliQA-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.MACEDONIAN, true),
    MUNICIPALI_DEMO("MunicipaliDemo-User", ProductTier.DEMO, ProductType.MUNICIPALI, Language.ENGLISH, Language.ENGLISH, false),
    MUNICIPALI_KARPOS("MunicipaliProd-Karpos-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.MACEDONIAN, false),
    MUNICIPALI_ARAD("MunicipaliProd-Arad-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.HEBREW, false),

    DEMOCRACY_QA("DemocracyQA-User", ProductTier.STANDALONE, ProductType.DEMOCRACY, Language.ENGLISH, Language.ENGLISH, true);

    @Getter private String productId;
    @Getter private ProductTier productTier;
    @Getter private ProductType productType;
    @Getter private Language language;
    @Getter private Language uiLanguage;
    @Getter private boolean qa;

    ProductConfiguration(
            String productId,
            ProductTier productTier,
            ProductType productType,
            Language language,
            Language uiLanguage,
            boolean qa
    ) {
        this.productId = productId;
        this.productTier = productTier;
        this.productType = productType;
        this.language = language;
        this.uiLanguage = uiLanguage;
        this.qa = qa;
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
