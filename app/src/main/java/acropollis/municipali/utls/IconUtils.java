package acropollis.municipali.utls;

import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipali.data.ScreenDensity;

@EBean
public class IconUtils {
    @Bean
    ScreenUtils screenUtils;

    // ToDo: use correct pixel-perfect sizes after fix on server
    public int getIconSize() {
        ScreenDensity screenDensity = screenUtils.getScreenDensity();

        switch (screenDensity) {
            case LDPI:
                return 100;
            case MDPI:
                return 100;
            case HDPI:
                return 200;
            case XHDPI:
                return 200;
            case XXHDPI:
                return 300;
            case XXXHDPI:
                return 400;
            default: {
                Log.e("IconUtils", "Unexpected density " + screenDensity);

                return 400;
            }
        }
    }
}
