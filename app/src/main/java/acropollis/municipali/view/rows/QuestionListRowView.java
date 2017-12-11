package acropollis.municipali.view.rows;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import acropollis.municipali.R;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipalidata.dto.answer.AnswerStatus;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.service.answer.UserAnswersService;

@EViewGroup(R.layout.view_questions_list_row)
public class QuestionListRowView extends LinearLayout {
    @ViewById(R.id.text)
    TextView textView;

    @Bean
    UserAnswersService userAnswersService;

    @Bean
    ProductConfigurationService productConfigurationService;

    public QuestionListRowView(Context context) {
        super(context);
    }

    public QuestionListRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(long articleId, TranslatedQuestion question) {
        textView.setText(question.getText());

        AnswerStatus answerStatus = userAnswersService.getAnswerStatus(
                productConfigurationService.getProductConfiguration(),
                articleId,
                question.getId()
        );

        if (answerStatus == AnswerStatus.ANSWERED) {
            setAlpha(0.5f);
        } else {
            setAlpha(1.0f);
        }
    }
}
