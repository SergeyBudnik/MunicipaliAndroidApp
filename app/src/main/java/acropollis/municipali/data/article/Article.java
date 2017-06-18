package acropollis.municipali.data.article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import acropollis.municipali.data.article.question.Question;
import acropollis.municipali.data.article.question.TranslatedQuestion;
import acropollis.municipali.data.common.Language;
import lombok.Data;

@Data
public class Article {
    private long id;
    private ArticleType type;
    private Map<Language, ArticleTranslationInfo> translatedArticle;
    private List<Question> questions;
    private boolean sendPushOnRelease;
    private long releaseDate;
    private long expirationDate;
    private long lastUpdateDate;
    private long creationDate;

    public boolean isTranslateable(Language language) {
        return translatedArticle.containsKey(language);
    }

    public TranslatedArticle translate(Language language) {
        TranslatedArticle res = new TranslatedArticle();

        res.setId(id);
        res.setType(type);
        res.setTitle(translatedArticle.get(language).getTitle());
        res.setText(translatedArticle.get(language).getText());
        res.setCategories(translatedArticle.get(language).getCategories());
        res.setQuestions(convertQuestions(language, questions));
        res.setReleaseDate(releaseDate);
        res.setExpirationDate(expirationDate);
        res.setLastUpdateDate(lastUpdateDate);

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
