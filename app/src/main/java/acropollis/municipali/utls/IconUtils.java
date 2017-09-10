package acropollis.municipali.utls;

import android.util.Log;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipali.data.ScreenDensity;

@EBean
public class IconUtils {
    @Bean
    ScreenUtils screenUtils;

    public int getIconSize() {
        ScreenDensity screenDensity = screenUtils.getScreenDensity();

        switch (screenDensity) {
            case LDPI:
                return 50;
            case MDPI:
                return 50;
            case HDPI:
                return 75;
            case XHDPI:
                return 100;
            case XXHDPI:
                return 150;
            case XXXHDPI:
                return 200;
            default: {
                Log.e("IconUtils", "Unexpected density " + screenDensity);

                return 200;
            }
        }
    }

    public int getArticleImageSize() {
        ScreenDensity screenDensity = screenUtils.getScreenDensity();

        switch (screenDensity) {
            case LDPI:
                return 150;
            case MDPI:
                return 225;
            case HDPI:
                return 300;
            case XHDPI:
                return 450;
            case XXHDPI:
                return 600;
            case XXXHDPI:
                return 600;
            default: {
                Log.e("IconUtils", "Unexpected density " + screenDensity);

                return 300;
            }
        }
    }
}
