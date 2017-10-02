package acropollis.municipali.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;

@EViewGroup(R.layout.view_questions_list_row)
public class QuestionListRowView extends LinearLayout {
    @ViewById(R.id.text)
    TextView textView;

    public QuestionListRowView(Context context) {
        super(context);
    }

    public QuestionListRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(TranslatedQuestion question) {
        textView.setText(question.getText());
    }
}
