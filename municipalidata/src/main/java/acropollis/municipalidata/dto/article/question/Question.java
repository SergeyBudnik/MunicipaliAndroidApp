package acropollis.municipalidata.dto.article.question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import acropollis.municipalidata.dto.article.question.answer.Answer;
import acropollis.municipalidata.dto.article.question.answer.TranslatedAnswer;
import acropollis.municipalidata.dto.common.Language;
import lombok.Data;

@Data
public class Question implements Serializable {
    private long id;
    private QuestionAnswerType answerType;
    private boolean showResult;
    private Map<Language, QuestionTranslationInfo> translatedQuestion;
    private List<Answer> answers;

    public boolean isTranslateable(Language language) {
        return translatedQuestion.containsKey(language);
    }

    public TranslatedQuestion translate(Language language) {
        TranslatedQuestion res = new TranslatedQuestion();

        res.setId(id);
        res.setAnswerType(answerType);
        res.setShowResult(showResult);
        res.setText(translatedQuestion.get(language).getText());
        res.setAnswers(convertAnswers(language, answers));

        return res;
    }

    private List<TranslatedAnswer> convertAnswers(Language language, List<Answer> answers) {
        List<TranslatedAnswer> res = new ArrayList<>();

        for (Answer answer : answers) {
            res.add(answer.translate(language));
        }

        return res;
    }
}
