package acropollis.municipali.view.events;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EViewGroup;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_event_calendar_selected_date)
public class EventCalendarSelectedDateView extends EventCalendarDateView {
    public EventCalendarSelectedDateView(Context context) {
        super(context);
    }

    public EventCalendarSelectedDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
