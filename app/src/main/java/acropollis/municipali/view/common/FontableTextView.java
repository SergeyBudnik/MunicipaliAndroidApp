package acropollis.municipali.view.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontableTextView extends TextView {
    private static final String SCHEME = "http://schemas.android.com/apk/res-auto";
    private static final String ATTRIBUTE = "font";

    public FontableTextView(Context context) {
        super(context);
    }

    public FontableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            String fontName = attrs.getAttributeValue(SCHEME, ATTRIBUTE);

            if (fontName != null) {
                Typeface customTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".ttf");

                setTypeface(customTypeface);
            }
        }
    }
}
