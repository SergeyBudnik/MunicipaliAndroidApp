package acropollis.municipali.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.androidannotations.annotations.EViewGroup;

import acropollis.municipali.R;

@EViewGroup(R.layout.fragment_menu)
public class MenuView extends LinearLayout {
    public MenuView(Context context) {
        super(context);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
