package acropollis.municipali.view.common;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

public class BackInterceptableRelativeLayout extends RelativeLayout {
    private Activity searchActivity;

    public BackInterceptableRelativeLayout(Context context) {
        super(context);
    }

    public BackInterceptableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSearchActivity(Activity searchActivity) {
        this.searchActivity = searchActivity;
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (searchActivity != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            KeyEvent.DispatcherState state = getKeyDispatcherState();

            if (state != null) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    state.startTracking(event, this);
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && state.isTracking(event)) {
                    searchActivity.onBackPressed();
                    return true;
                }
            }
        }

        return super.dispatchKeyEventPreIme(event);
    }
}
