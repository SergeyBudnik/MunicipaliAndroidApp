package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;

import static acropollis.municipali.utls.BitmapUtils.iconFromBytes;

@EViewGroup(R.layout.view_round_icon)
public class MunicipaliIconView extends RelativeLayout {
    @ViewById(R.id.icon_placeholder)
    View iconPlaceholderView;
    @ViewById(R.id.icon)
    ImageView iconView;

    public MunicipaliIconView(Context context) {
        super(context);
    }

    public MunicipaliIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @UiThread
    public void setIcon(byte [] icon) {
        iconView.setImageBitmap(iconFromBytes(icon));

        iconView.setVisibility(VISIBLE);
        iconPlaceholderView.setVisibility(GONE);
    }

    @UiThread
    public void hideIcon() {
        iconPlaceholderView.setVisibility(VISIBLE);
        iconView.setVisibility(GONE);
    }
}
