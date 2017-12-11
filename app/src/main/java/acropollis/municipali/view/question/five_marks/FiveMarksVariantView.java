package acropollis.municipali.view.question.five_marks;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annimon.stream.function.Consumer;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;

@EViewGroup(R.layout.view_five_marks_variant)
public class FiveMarksVariantView extends RelativeLayout {
    @ViewById(R.id.vote_text)
    TextView voteTextView;
    @ViewById(R.id.vote_layout)
    View voteLayoutView;

    public FiveMarksVariantView(Context context) {
        super(context);
    }

    public FiveMarksVariantView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(int text, Consumer<Void> onClickListener) {
        voteTextView.setText(text);

        setOnClickListener(it -> onClickListener.accept(null));
    }
    
    public void setSelected(boolean selected) {
        voteLayoutView.setBackgroundResource(selected ? R.drawable.oval_red : R.drawable.oval_gray);
    }
}
