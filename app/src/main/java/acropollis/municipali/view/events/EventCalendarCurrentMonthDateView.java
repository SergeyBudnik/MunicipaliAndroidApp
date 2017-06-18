package acropollis.municipali.view.events;

import android.content.Context;
import android.util.AttributeSet;

import org.androidannotations.annotations.EViewGroup;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_event_calendar_current_month_date)
public class EventCalendarCurrentMonthDateView extends EventCalendarDateView {
    public EventCalendarCurrentMonthDateView(Context context) {
        super(context);
    }

    public EventCalendarCurrentMonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
