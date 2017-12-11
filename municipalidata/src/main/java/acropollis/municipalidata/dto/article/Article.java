package acropollis.municipalidata.dto.article;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import acropollis.municipalidata.dto.article.question.Question;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipalidata.dto.common.Language;
import lombok.Data;

@Data
public class Article implements Serializable {
    private long id;
    private ArticleType type;
    private Map<Language, ArticleTranslationInfo> translatedArticle;
    private List<Question> questions;
    private String video;
    private boolean sendPushOnRelease;
    private long creationDate;
    private long releaseDate;
    private long calendarStartDate;
    private long calendarFinishDate;
    private long expirationDate;
    private long lastUpdateDate;

    public boolean isTranslateable(Language language) {
        return translatedArticle.containsKey(language);
    }

    public TranslatedArticle translate(Language language) {
        TranslatedArticle res = new TranslatedArticle();

        res.setId(id);
        res.setType(type);
        res.setTitle(translatedArticle.get(language).getTitle());
        res.setDescription(translatedArticle.get(language).getDescription());
        res.setText(translatedArticle.get(language).getText());
        res.setCategories(translatedArticle.get(language).getCategories());
        res.setVideo(video);
        res.setQuestions(convertQuestions(language, questions));
        res.setReleaseDate(releaseDate);
        res.setExpirationDate(expirationDate);
        res.setLastUpdateDate(lastUpdateDate);
        res.setCalendarStartDate(calendarStartDate);
        res.setCalendarStartDate(calendarFinishDate);

        return res;
    }

    private Map<Long, TranslatedQuestion> convertQuestions(Language language, List<Question> questions) {
        Map<Long, TranslatedQuestion> res = new HashMap<>();

        for (Question question : questions) {
            if (question.isTranslateable(language)) {
                res.put(question.getId(), question.translate(language));
            }
        }

        return res;
    }
}
