package acropollis.municipali.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_calendar_date)
public class CalendarDateView extends RelativeLayout {
    @ViewById(R.id.text)
    TextView textView;

    @ViewById(R.id.background)
    View backgroundView;
    @ViewById(R.id.left_background)
    View rightBackgroundView;
    @ViewById(R.id.right_background)
    View leftBackgroundView;

    public CalendarDateView(Context context) {
        super(context);
    }

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setDefault() {
        setDateStyle(R.color.black, 0, 0, 0);
    }

    public void setToday() {
        setDateStyle(R.color.red, 0, 0, 0);
    }

    public void setOtherMonthDate() {
        setVisibility(GONE);
    }

    public void setArticleDate() {
        setDateStyle(R.color.red, R.drawable.circle_red, 0, 0);
    }

    public void setSingleSelectedDate() {
        setDateStyle(R.color.white, R.drawable.oval_red, 0, 0);
    }

    public void setIntervalLeftSelectedDate() {
        setDateStyle(R.color.white, R.drawable.oval_red, R.color.red, 0);
    }

    public void setIntervalRightSelectedDate() {
        setDateStyle(R.color.white, R.drawable.oval_red, 0, R.color.red);
    }

    public void setIntervalCenterSelectedDate() {
        setDateStyle(R.color.white, R.drawable.oval_red, R.color.red, R.color.red);
    }

    private void setDateStyle(int textColorRes, int backgroundRes, int leftBackgroundRes, int rightBackgroundRes) {
        textView.setTextColor(getResources().getColor(textColorRes));

        backgroundView.setBackgroundResource(backgroundRes);
        leftBackgroundView.setBackgroundResource(leftBackgroundRes);
        rightBackgroundView.setBackgroundResource(rightBackgroundRes);
    }
}
