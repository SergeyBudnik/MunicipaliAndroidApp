package acropollis.municipalibootstrap.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EView;

import acropollis.municipalibootstrap.R;
import lombok.Setter;

@EView
public class MunicipaliProportionsView extends RelativeLayout {
    private int ratioBasis;
    @Setter private int widthRario;
    @Setter private int heightRatio;

    public MunicipaliProportionsView(Context context) {
        super(context);
    }

    public MunicipaliProportionsView(Context context, AttributeSet attrs) {
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
}
