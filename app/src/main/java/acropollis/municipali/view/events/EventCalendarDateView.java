package acropollis.municipali.view.events;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;

@EViewGroup
public abstract class EventCalendarDateView extends RelativeLayout {
    @ViewById(R.id.text)
    TextView textView;

    public EventCalendarDateView(Context context) {
        super(context);
    }

    public EventCalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
