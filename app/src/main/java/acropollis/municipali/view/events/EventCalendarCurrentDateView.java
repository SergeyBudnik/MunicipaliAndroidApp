package acropollis.municipali.view.events;

import android.content.Context;
import android.util.AttributeSet;

import org.androidannotations.annotations.EViewGroup;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_event_calendar_current_date)
public class EventCalendarCurrentDateView extends EventCalendarDateView {
    public EventCalendarCurrentDateView(Context context) {
        super(context);
    }

    public EventCalendarCurrentDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
