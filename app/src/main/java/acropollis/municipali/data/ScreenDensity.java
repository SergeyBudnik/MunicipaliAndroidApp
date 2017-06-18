package acropollis.municipali.data;

import lombok.Getter;

public enum ScreenDensity {
    LDPI(ImageSize.SIZE_0_75,   0.75),
    MDPI(ImageSize.SIZE_1_00,   1.00),
    HDPI(ImageSize.SIZE_1_50,   1.50),
    XHDPI(ImageSize.SIZE_2_00,  2.00),
    XXHDPI(ImageSize.SIZE_3_00, 3.00),
    XXXHDPI(ImageSize.SIZE_4_00, 4.00);

    @Getter private ImageSize imageSize;
    @Getter private double pxInDp;

    ScreenDensity(ImageSize imageSize, double pxInDp) {
        this.imageSize = imageSize;
        this.pxInDp = pxInDp;
    }
}
