package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.util.AttributeSet;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.rest.RestService;

import acropollis.municipali.R;
import acropollis.municipali.bootstrap.data.MunicipaliLoadableIconData;
import acropollis.municipali.rest.raw.common.ImageRestService;

@EViewGroup(R.layout.view_round_icon)
public class MunicipaliLoadableIconView extends MunicipaliIconView {
    @RestService
    ImageRestService imageRestService;

    private MunicipaliLoadableIconData loadableIconData;

    public MunicipaliLoadableIconView(Context context) {
        super(context);
    }

    public MunicipaliLoadableIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public synchronized void setIcon(MunicipaliLoadableIconData loadableIconData) {
        this.loadableIconData = loadableIconData;

        byte [] iconFromCache = loadableIconData.getIconFromCacheLoader() != null ?
                loadableIconData.getIconFromCacheLoader().load() : null;

        if (iconFromCache != null) {
            if (iconFromCache.length != 0) {
                setIcon(iconFromCache);
            } else {
                hideIcon();
            }
        } else {
            hideIcon();

            loadIcon(loadableIconData);
        }
    }

    @Background
    void loadIcon(final MunicipaliLoadableIconData loadableIconData) {
        loadableIconData.getIconFromNetworkLoader().load(new MunicipaliLoadableIconData.IconLoadingListener() {
            @Override
            public void onSuccess(byte[] icon) {
                synchronized (MunicipaliLoadableIconView.this) {
                    boolean isSameData =
                            loadableIconData.getId() == MunicipaliLoadableIconView.this.loadableIconData.getId();

                    if (isSameData) {
                        if (icon.length != 0) {
                            setIcon(icon);
                        } else {
                            hideIcon();
                        }
                    }
                }
            }

            @Override
            public void onFailure() {
                /* Do nothing */
            }
        });
    }
}
