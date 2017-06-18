package acropollis.municipali.drawers.history;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;

import java.text.SimpleDateFormat;

import acropollis.municipali.R;

public class HistoryDrawer {
    private HistoryData [] historyData;
    private int selectedPointX;

    public void setHistory(HistoryData [] historyData) {
        this.historyData = historyData;
    }

    public void setSelectedPoint(int selectedPointX) {
        this.selectedPointX = selectedPointX;
    }

    public void draw(Context context, Canvas c) {
        c.drawColor(Color.TRANSPARENT);

        drawHistory(context, c);
    }

    private void drawHistory(Context context, Canvas c) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        Paint goodRatePaint = new Paint();

        goodRatePaint.setColor(context.getResources().getColor(R.color.primary_blue));
        goodRatePaint.setAntiAlias(true);

        Paint badRatePaint = new Paint();

        badRatePaint.setColor(context.getResources().getColor(R.color.primary_orange));
        badRatePaint.setAntiAlias(true);

        Paint clarificationOvalPaint = new Paint();

        clarificationOvalPaint.setAntiAlias(true);
        clarificationOvalPaint.setStyle(Paint.Style.FILL);

        UnivariateFunction s = createSpline(c);

        int intervalWidth = c.getWidth() / (historyData.length + 1);
        int selectedPointXWithOffset = selectedPointX - intervalWidth / 2;

        int normalizedSelectedPoint = selectedPointXWithOffset / intervalWidth;

        if (normalizedSelectedPoint < 0) {
            normalizedSelectedPoint = 0;
        } else if (normalizedSelectedPoint > historyData.length - 1) {
            normalizedSelectedPoint = historyData.length - 1;
        }

        int centerX = normalizedSelectedPoint * intervalWidth + intervalWidth;
        int centerY = c.getHeight() / 2;

        for (int x = 0; x < c.getWidth(); x++) {
            int y = (int) s.value(x);

            c.drawLine(x, centerY, x, c.getHeight() - y, y > centerY ? goodRatePaint : badRatePaint);
        }

        int circleY = (int) s.value(centerX);

        int radius = 30;

        boolean underTheLine = circleY > centerY;

        clarificationOvalPaint.setColor(
                underTheLine ? Color.rgb(67, 139, 184) : Color.rgb(253, 112, 52));

        c.drawCircle(centerX, c.getHeight() - circleY, radius, clarificationOvalPaint);

        String text = sdf.format(historyData[normalizedSelectedPoint].getDate());

        Rect textBoundsRect = new Rect();

        Paint p = new Paint();

        p.setTextSize(60);
        p.setAntiAlias(true);
        p.getTextBounds(text, 0, text.length(), textBoundsRect);

        if (underTheLine) {
            c.drawText(text, centerX - textBoundsRect.width() / 2, c.getHeight() - (circleY - 2 * radius - textBoundsRect.height()), p);
        } else {
            c.drawText(text, centerX - textBoundsRect.width() / 2, c.getHeight() - (circleY + 2 * radius), p);
        }
    }

    private UnivariateFunction createSpline(Canvas c) {
        double [] x = new double [2 + 3 * historyData.length];
        double [] y = new double [2 + 3 * historyData.length];

        x[0] = 0;
        y[0] = c.getHeight() / 2;

        x[x.length - 1] = c.getWidth();
        y[y.length - 1] = c.getHeight() / 2;

        int padding = 0;
        int height = c.getHeight() - 2 * padding;

        for (int i = 0; i < historyData.length; i++) {
            int xCoord = (i + 1) * c.getWidth() / (historyData.length + 1);
            int yCoord = padding + (int) ((historyData[i].getRate() - 1) * height / 4);

            x[3 * i + 1] = xCoord - 1;
            y[3 * i + 1] = yCoord;

            x[3 * i + 2] = xCoord;
            y[3 * i + 2] = yCoord;

            x[3 * i + 3] = xCoord + 1;
            y[3 * i + 3] = yCoord;
        }

        return new SplineInterpolator().interpolate(x, y);
    }
}
