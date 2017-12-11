package acropollis.municipali.view.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.androidannotations.annotations.EView;

import acropollis.municipali.R;

@EView
public class SegmentedView extends View {
    private int percents = 10;

    private Paint paint = new Paint();
    private RectF rect = new RectF();

    public SegmentedView(Context context) {
        super(context);
    }

    public SegmentedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPercents(int percents) {
        this.percents = percents;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        paint.setColor(getContext().getResources().getColor(R.color.gray_3));
        rect.set(0, 0, width, height);

        int drawAngle = 360 - 360 * percents / 100;

        canvas.drawArc(rect, -90 - drawAngle, drawAngle, true, paint);
    }
}
