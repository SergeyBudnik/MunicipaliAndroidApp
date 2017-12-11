package acropollis.municipali.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_header)
public class HeaderView extends RelativeLayout {
    public enum Style {
        DEFAULT, TRANSPARENT
    }

    @ViewById(R.id.header)
    View headerView;
    @ViewById(R.id.header_title)
    TextView headerTitleView;

    @ViewById(R.id.header_left_button)
    ImageView headerLeftButtonView;
    @ViewById(R.id.header_right_button)
    ImageView headerRightButtonView;

    public HeaderView(Context context) {
        super(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStyle(Style style) {
        setBackgroundResource(style == Style.DEFAULT ? R.color.red : R.color.black_semi_transparent_3);
    }

    public void setTitle(int titleResource) {
        headerTitleView.setText(titleResource);
    }

    public void setLeftButton(int imageResource, OnClickListener listener) {
        setButton(headerLeftButtonView, imageResource, listener);
    }

    public void setRightButton(int imageResource, OnClickListener listener) {
        setButton(headerRightButtonView, imageResource, listener);
    }

    public void removeRightButton() {
        headerRightButtonView.setVisibility(GONE);
    }

    private void setButton(ImageView buttonView, int imageResource, OnClickListener listener) {
        buttonView.setImageResource(imageResource);
        buttonView.setOnClickListener(listener);
        buttonView.setVisibility(VISIBLE);
    }
}
