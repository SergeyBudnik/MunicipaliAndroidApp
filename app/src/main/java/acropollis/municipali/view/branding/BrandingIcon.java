package acropollis.municipali.view.branding;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipalidata.service.branding.BrandingService;

import static acropollis.municipali.utls.BitmapUtils.iconFromBytes;

@EViewGroup(R.layout.view_branding_icon)
public class BrandingIcon extends RelativeLayout {
    @ViewById(R.id.icon)
    ImageView iconView;

    @Bean
    BrandingService brandingService;
    @Bean
    ProductConfigurationService productConfigurationService;

    public BrandingIcon(Context context) {
        super(context);
    }

    public BrandingIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init() {
        setBackground();
    }

    private void setBackground() {
        if (!isInEditMode()) {
            brandingService.getIcon(
                    productConfigurationService.getProductConfiguration()
            ).ifPresent(it ->
                    iconView.post(() -> iconView.setImageBitmap(iconFromBytes(it)))
            );
        }
    }
}
