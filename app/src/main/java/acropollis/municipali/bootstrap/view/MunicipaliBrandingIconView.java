package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipali.data.backend.BackendInfo;
import acropollis.municipali.utls.BitmapUtils;

@EViewGroup(R.layout.bootstrap_view_branding_icon)
public class MunicipaliBrandingIconView extends RelativeLayout {
    @ViewById(R.id.icon)
    ImageView iconView;

    public MunicipaliBrandingIconView(Context context) {
        super(context);
    }

    public MunicipaliBrandingIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init() {
        setBackground();
    }

    private void setBackground() {
        if (!isInEditMode()) {
            final BackendInfo backendInfo = null;

            if (backendInfo != null && backendInfo.getIcon() != null) {
                iconView.post(new Runnable() {
                    @Override
                    public void run() {
                        iconView.setImageBitmap(BitmapUtils.iconFromBytes(backendInfo.getIcon()));
                    }
                });
            }
        }
    }
}
