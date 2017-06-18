package acropollis.municipali.view.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_popup_message)
public class PopupMessageView extends RelativeLayout {
    @ViewById(R.id.text)
    TextView textView;

    public PopupMessageView(Context context) {
        super(context);
    }

    public PopupMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupMessageView bind(String text) {
        textView.setText(text);

        return this;
    }
}
