package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipali.data.backend.BackendInfo;
import acropollis.municipali.service.BackendInfoService;
import acropollis.municipali.utls.BitmapUtils;

@EViewGroup(R.layout.bootstrap_view_branding_background)
public class MunicipaliBrandingBackgroundView extends RelativeLayout {
    @Bean
    BackendInfoService backendInfoService;

    @ViewById(R.id.background)
    ImageView backgroundView;

    public MunicipaliBrandingBackgroundView(Context context) {
        super(context);
    }

    public MunicipaliBrandingBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init() {
        setBackground();
    }

    private void setBackground() {
        if (!isInEditMode()) {
            final BackendInfo backendInfo = backendInfoService.getBackendInfo();

            if (backendInfo != null && backendInfo.getBackground() != null) {
                backgroundView.post(new Runnable() {
                    @Override
                    public void run() {
                        backgroundView.setImageBitmap(BitmapUtils.iconFromBytes(backendInfo.getBackground()));
                    }
                });
            }
        }
    }
}
