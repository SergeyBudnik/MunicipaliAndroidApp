package acropollis.municipali.data.article.question;

import java.util.List;

import acropollis.municipali.data.article.question.answer.TranslatedAnswer;
import lombok.Data;

@Data
public class TranslatedQuestion {
    private long id;
    private QuestionAnswerType answerType;
    private boolean showResult;
    private String text;
    private List<TranslatedAnswer> answers;
}
