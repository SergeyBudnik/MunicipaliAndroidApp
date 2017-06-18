package acropollis.municipali.view.events;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EViewGroup;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_event_calendar_special_date)
public class EventCalendarSpecialDateView extends EventCalendarDateView {
    public EventCalendarSpecialDateView(Context context) {
        super(context);
    }

    public EventCalendarSpecialDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
