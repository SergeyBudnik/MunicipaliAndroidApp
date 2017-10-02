package acropollis.municipalidata.utils;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import lombok.Getter;

@EBean
public class ScreenUtils {
    public enum Density {
        LDPI(0.75),
        MDPI(1.00),
        HDPI(1.50),
        XHDPI(2.00),
        XXHDPI(3.00),
        XXXHDPI(4.00);

        @Getter private double pxInDp;

        Density(double pxInDp) {
            this.pxInDp = pxInDp;
        }
    }


    @RootContext
    Context context;

    public Density getScreenDensity() {
        double density = context.getResources().getDisplayMetrics().density;

        if (density < 0.85) {
            return Density.LDPI;
        } else if (density < 1.30) {
            return Density.MDPI;
        } else if (density < 1.75) {
            return Density.HDPI;
        } else if (density < 2.50) {
            return Density.XHDPI;
        } else if (density < 3.50) {
            return Density.XXHDPI;
        } else {
            return Density.XXXHDPI;
        }
    }
}
