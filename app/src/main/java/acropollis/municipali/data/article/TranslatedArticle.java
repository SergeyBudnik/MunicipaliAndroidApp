package acropollis.municipali.data.article;

import java.util.List;
import java.util.Map;

import acropollis.municipali.data.article.question.TranslatedQuestion;
import lombok.Data;

@Data
public class TranslatedArticle {
    private long id;
    private ArticleType type;
    private String title;
    private String text;
    private List<String> categories;
    private Map<Long, TranslatedQuestion> questions;
    private long releaseDate;
    private long expirationDate;
    private long lastUpdateDate;
}
