package acropollis.municipali.view.branding;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipali.data.backend.BackendInfo;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipali.utls.BitmapUtils;
import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.service.branding.BrandingService;

import static acropollis.municipali.utls.BitmapUtils.iconFromBytes;

@EViewGroup(R.layout.view_branding_background)
public class BrandingBackground extends RelativeLayout {
    @ViewById(R.id.background)
    ImageView backgroundView;

    @Bean
    BrandingService brandingService;
    @Bean
    ProductConfigurationService productConfigurationService;

    public BrandingBackground(Context context) {
        super(context);
    }

    public BrandingBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init() {
        setBackground();
    }

    private void setBackground() {
        if (!isInEditMode()) {
            brandingService.getBackground(
                    productConfigurationService.getProductConfiguration()
            ).ifPresent(it ->
                backgroundView.post(() -> backgroundView.setImageBitmap(iconFromBytes(it)))
            );
        }
    }
}
