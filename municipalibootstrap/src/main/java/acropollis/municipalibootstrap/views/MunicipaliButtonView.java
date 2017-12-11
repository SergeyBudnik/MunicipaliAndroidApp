package acropollis.municipalibootstrap.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

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

    public enum State {
        ENABLED, DISABLED, LOADING
    }

    private String text;
    private State state;
    private Type type;

    @ViewById(resName = "text")
    TextView textView;
    @ViewById(resName = "loading")
    View loadingView;

    @AnimationRes(resName = "loader")
    Animation loaderAnim;

    public MunicipaliButtonView(Context context) {
        super(context);
    }

    public MunicipaliButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.MunicipaliButtonView); {
            text = array.getString(R.styleable.MunicipaliButtonView_text);
            state = array.getBoolean(R.styleable.MunicipaliButtonView_enabled, true) ? State.ENABLED : State.DISABLED;
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

        setEnabledStyle(state == State.ENABLED);
    }

    public void setEnabled(boolean enabled) {
        this.state = enabled ? State.ENABLED : State.DISABLED;

        setEnabledStyle(enabled);
        setLoadingStyle(false);
    }

    public void setLoading(boolean loading) {
        this.state = loading ? State.ENABLED : State.LOADING;

        setEnabledStyle(true);
        setLoadingStyle(loading);
    }

    private void setEnabledStyle(boolean enabled) {
        if (enabled) {
            setAlpha(1.0f);
        } else {
            setAlpha(0.5f);
        }
    }

    private void setLoadingStyle(boolean loading) {
        if (loading) {
            textView.setVisibility(GONE);

            loadingView.setAnimation(loaderAnim);
            loadingView.setVisibility(VISIBLE);
        } else {
            textView.setVisibility(VISIBLE);

            loadingView.clearAnimation();
            loadingView.setVisibility(INVISIBLE);
        }
    }
}
