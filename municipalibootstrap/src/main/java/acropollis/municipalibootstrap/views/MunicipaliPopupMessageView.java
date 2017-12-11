package acropollis.municipalibootstrap.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipalibootstrap.R;

@EViewGroup(resName = "view_popup_message")
public class MunicipaliPopupMessageView extends RelativeLayout {
    public enum Type {
        SUCCESS, ERROR
    }

    @ViewById(resName = "text")
    TextView textView;

    public MunicipaliPopupMessageView(Context context) {
        super(context);
    }

    public MunicipaliPopupMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(String text, Type type) {
        textView.setText(text);

        textView.setTextColor(getTextColor(type));
        textView.setBackgroundResource(getBackgroundResource(type));
    }

    private int getTextColor(Type type) {
        switch (type) {
            case SUCCESS:
                return getResources().getColor(R.color.white);
            case ERROR:
                return getResources().getColor(R.color.white);
        }

        throw new RuntimeException();
    }

    private int getBackgroundResource(Type type) {
        switch (type) {
            case SUCCESS:
                return R.drawable.popup_success;
            case ERROR:
                return R.drawable.popup_failure;
        }

        throw new RuntimeException();
    }
}
