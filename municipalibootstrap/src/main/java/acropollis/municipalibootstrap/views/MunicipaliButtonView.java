package acropollis.municipalibootstrap.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipalibootstrap.R;

@EViewGroup(resName = "view_municipali_button")
public class MunicipaliButtonView extends RelativeLayout {
    private enum Type {
        PRIMARY(1), DEFAULT(2);

        public int code;

        Type(int code) {
            this.code = code;
        }

        public static Type fromCode(int code) {
            for (Type t : values()) {
                if (t.code == code) {
                    return t;
                }
            }

            return PRIMARY;
        }
    }

    private String text;
    private boolean enabled;
    private Type type;

    @ViewById(resName = "text")
    TextView textView;

    public MunicipaliButtonView(Context context) {
        super(context);
    }

    public MunicipaliButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MunicipaliButtonView); {
            text = array.getString(R.styleable.MunicipaliButtonView_text);
            enabled = array.getBoolean(R.styleable.MunicipaliButtonView_enabled, true);
            type = Type.fromCode(array.getInt(R.styleable.MunicipaliButtonView_type, 1));

            array.recycle();
        }
    }

    @AfterViews
    public void init() {
        textView.setText(text);

        switch (type) {
            case PRIMARY:
                textView.setBackgroundResource(R.drawable.button_primary);
                textView.setTextColor(getResources().getColor(R.color.white));
                break;
            case DEFAULT:
                textView.setBackgroundResource(R.drawable.button_default);
                textView.setTextColor(getResources().getColor(R.color.red));
                break;
        }
    }
}
