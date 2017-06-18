package acropollis.municipali.view.events;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EViewGroup;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_event_calendar_other_month_date)
public class EventCalendarOtherMonthDateView extends EventCalendarDateView {
    public EventCalendarOtherMonthDateView(Context context) {
        super(context);
    }

    public EventCalendarOtherMonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
