package acropollis.municipali.data.article.question.answer;

import java.io.Serializable;
import java.util.Map;

import acropollis.municipali.data.common.Language;
import lombok.Data;

@Data
public class Answer implements Serializable {
    private long id;
    private Map<Language, AnswerTranslationInfo> translatedAnswer;

    public TranslatedAnswer translate(Language language) {
        TranslatedAnswer res = new TranslatedAnswer();

        res.setId(id);
        res.setText(
                translatedAnswer.containsKey(language) ?
                translatedAnswer.get(language).getText() :
                ""
        );

        return res;
    }
}
