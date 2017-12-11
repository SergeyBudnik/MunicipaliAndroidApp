package acropollis.municipalibootstrap.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.annimon.stream.Objects;
import com.annimon.stream.function.Supplier;
import com.makeramen.roundedimageview.RoundedImageView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.UiThread;

import acropollis.municipalibootstrap.R;
import lombok.Setter;

import static acropollis.municipalibootstrap.utils.BitmapUtils.iconFromBytes;

@EView
public class MunicipaliLoadableImageView extends RoundedImageView {
    private int ratioBasis;
    @Setter private int widthRario;
    @Setter private int heightRatio;

    private String id;

    public MunicipaliLoadableImageView(Context context) {
        super(context);
    }

    public MunicipaliLoadableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MunicipaliLoadableImageView); {
            ratioBasis = array.getInt(R.styleable.MunicipaliLoadableImageView_ratioBasis, 0);
            widthRario = array.getInt(R.styleable.MunicipaliLoadableImageView_widthRatio, 0);
            heightRatio = array.getInt(R.styleable.MunicipaliLoadableImageView_heightRatio, 0);

            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (ratioBasis == 0 || widthRario == 0 || heightRatio == 0) {
            setMeasuredDimension(widthSize, heightSize);
        } else {
            if (ratioBasis == 1) {
                setMeasuredDimension(widthSize, widthSize * heightRatio / widthRario);
            } else if (ratioBasis == 2) {
                setMeasuredDimension(heightSize * widthRario / heightRatio, heightSize);
            }
        }
    }

    public void configure(
            String id,
            int emptyResource,
            Supplier<byte []> cacheProvider,
            Supplier<byte []> loadingProvider
    ) {
        this.id = id;

        loadImageFromCache(id, emptyResource, cacheProvider, loadingProvider);
    }

    @Background
    void loadImageFromCache(String id, int emptyResource, Supplier<byte []> cacheProvider, Supplier<byte []> loadingProvider) {
        byte [] imageFromCache = cacheProvider.get();

        if (imageFromCache != null) {
            if (imageFromCache.length == 0) {
                setImage(emptyResource);
            } else {
                setImage(iconFromBytes(imageFromCache));
            }
        } else {
            setImage(emptyResource);

            loadImageFromNetwork(id, emptyResource, loadingProvider);
        }
    }

    void loadImageFromNetwork(String id, int emptyResource, Supplier<byte []> loadingProvider) {
        byte [] image = loadingProvider.get();

        synchronized (MunicipaliLoadableImageView.class) {
            if (Objects.equals(this.id, id)) {
                if (image == null || image.length == 0) {
                    setImage(emptyResource);
                } else {
                    setImage(iconFromBytes(image));
                }
            }
        }
    }

    @UiThread
    void setImage(int resource) {
        setImageResource(resource);
    }

    @UiThread
    void setImage(Bitmap image) {
        setImageBitmap(image);
    }
}
