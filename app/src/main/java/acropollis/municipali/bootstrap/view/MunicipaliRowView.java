package acropollis.municipali.bootstrap.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipali.bootstrap.data.MunicipaliLoadableIconData;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipalibootstrap.views.MunicipaliLoadableImageView;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;

import static acropollis.municipali.utls.BitmapUtils.iconFromBytes;

@EViewGroup(R.layout.bootstrap_view_municipali_row)
public class MunicipaliRowView extends RelativeLayout {
    private static final float UNTRANSPARENT_ALPHA = 1.0f;
    private static final float TRANSPARENT_ALPHA = 0.6f;

    @ViewById(R.id.text)
    TextView textView;
    @ViewById(R.id.image)
    MunicipaliLoadableImageView imageView;
//    @ViewById(R.id.loadable_icon)
//    MunicipaliLoadableIconView loadableIconView;

//    @ViewById(R.id.unanswered_questions_counter_container)
//    View unansweredQuestionsCounterContainerView;
//    @ViewById(R.id.unanswered_questions_counter)
//    TextView unansweredQuestionsCounterView;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    private MunicipaliRowData rowData;

    public MunicipaliRowView(Context context) {
        super(context);
    }

    public MunicipaliRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(MunicipaliRowData rowData) {
        this.rowData = rowData;

        textView.setText(rowData.getText());

        imageView.configure(
                String.valueOf(rowData.getId()),
                R.color.light_gray,
                () -> rowData.getLoadableIconData().getIconFromCacheLoader() != null ? rowData.getLoadableIconData().getIconFromCacheLoader().load() : null,
                () -> {
                    articlesRestWrapper.loadArticleImage(null, rowData.getId());

                    return null;
                }
        );

        setIcon();
//        if (rowData.getLoadableIconData() != null) {
//            loadableIconView.setIcon(rowData.getLoadableIconData());
//        }
//
//        if (rowData.getTransparencyInfo().isTransparent()) {
//            textView.setAlpha(TRANSPARENT_ALPHA);
//            loadableIconView.setAlpha(TRANSPARENT_ALPHA);
//        } else {
//            textView.setAlpha(UNTRANSPARENT_ALPHA);
//            loadableIconView.setAlpha(UNTRANSPARENT_ALPHA);
//        }
//
//        if (rowData.getCounterInfo().getCounter() == 0) {
//            unansweredQuestionsCounterContainerView.setVisibility(GONE);
//        } else {
//            unansweredQuestionsCounterContainerView.setVisibility(VISIBLE);
//            unansweredQuestionsCounterView.setText("" + rowData.getCounterInfo().getCounter()); // ToDo
//        }
    }

    public MunicipaliRowData getRowData() {
        return rowData;
    }

    synchronized void setIcon() {
        byte [] iconFromCache = rowData.getLoadableIconData().getIconFromCacheLoader() != null ?
                rowData.getLoadableIconData().getIconFromCacheLoader().load() : null;

        if (iconFromCache != null) {
            if (iconFromCache.length != 0) {
                setIcon(iconFromBytes(iconFromCache));
            } else {
                setIcon(null);
            }
        } else {
            setIcon(null);

            loadIcon(rowData.getLoadableIconData());
        }
    }

    @Background
    void loadIcon(final MunicipaliLoadableIconData loadableIconData) {
        loadableIconData.getIconFromNetworkLoader().load(new MunicipaliLoadableIconData.IconLoadingListener() {
            @Override
            public void onSuccess(byte[] icon) {
                synchronized (MunicipaliRowView.this) {
                    boolean isSameData =
                            loadableIconData.getId() == MunicipaliRowView.this.rowData.getLoadableIconData().getId();

                    if (isSameData) {
                        if (icon.length != 0) {
                            setIcon(iconFromBytes(icon));
                        } else {
                            setIcon(null);
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

    @UiThread
    void setIcon(Bitmap iconBitmap) {
        imageView.setImageBitmap(iconBitmap);
    }

    public void setRedPalette() {
        // ToDo
        int[] attribute = new int[] { R.attr.text_primary_color };
        TypedArray array = getContext().getTheme().obtainStyledAttributes(attribute);
        int color = array.getColor(0, Color.TRANSPARENT);
        array.recycle();

        textView.setTextColor(color);
    }

    public void setWhitePalette() {
        textView.setTextColor(getResources().getColor(R.color.primary_black));
    }
}
