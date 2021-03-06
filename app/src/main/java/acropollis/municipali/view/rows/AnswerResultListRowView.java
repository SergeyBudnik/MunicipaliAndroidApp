package acropollis.municipali.view.rows;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipalidata.dto.article.question.answer.TranslatedAnswer;

@EViewGroup(R.layout.view_answer_list_row)
public class AnswerResultListRowView extends LinearLayout {
    @ViewById(R.id.text)
    TextView textView;

    private boolean selected;

    private TranslatedAnswer answer;

    public AnswerResultListRowView(Context context) {
        super(context);
    }

    public AnswerResultListRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(TranslatedAnswer answer) {
        this.answer = answer;

        textView.setText(answer.getText());
    }

    public void setPercents(int percents) {
        textView.setText(answer.getText() + " - " + percents);
    }

    public TranslatedAnswer getAnswer() {
        return answer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;

        textView.setTextColor(getResources().getColor(selected ? R.color.white : R.color.white_semi_transparent_2));
    }
}
