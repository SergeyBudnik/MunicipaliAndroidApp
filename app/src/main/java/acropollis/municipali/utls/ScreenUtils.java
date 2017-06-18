package acropollis.municipali.utls;

import android.content.Context;
import android.util.DisplayMetrics;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import acropollis.municipali.data.ScreenDensity;

@EBean
public class ScreenUtils {
    @RootContext
    Context context;

    public ScreenDensity getScreenDensity() {
        double density = context.getResources().getDisplayMetrics().density;

        if (density < 0.85) {
            return ScreenDensity.LDPI;
        } else if (density < 1.30) {
            return ScreenDensity.MDPI;
        } else if (density < 1.75) {
            return ScreenDensity.HDPI;
        } else if (density < 2.50) {
            return ScreenDensity.XHDPI;
        } else if (density < 3.50) {
            return ScreenDensity.XXHDPI;
        } else {
            return ScreenDensity.XXXHDPI;
        }
    }

    public int dpToPx(int dp) {
        return (int) (dp * getScreenDensity().getPxInDp());
    }
}
