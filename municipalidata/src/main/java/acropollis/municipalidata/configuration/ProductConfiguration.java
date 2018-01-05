package acropollis.municipalidata.configuration;

import acropollis.municipalidata.dto.common.Language;
import lombok.Getter;

public enum ProductConfiguration {
    MUNICIPALI_QA("MunicipaliQA-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.ENGLISH, true),
    MUNICIPALI_DEMO("MunicipaliDemo-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.ENGLISH, false),
    MUNICIPALI_ISRAEL_DEMO("MunicipaliDemoIsrael-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.ENGLISH, Language.ENGLISH, false),
    MUNICIPALI_KARPOS("MunicipaliProdKarpos-User", ProductTier.STANDALONE, ProductType.MUNICIPALI, Language.MACEDONIAN, Language.MACEDONIAN, false),

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
