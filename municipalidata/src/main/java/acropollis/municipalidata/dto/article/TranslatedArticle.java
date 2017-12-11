package acropollis.municipalidata.dto.article;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import lombok.Data;

@Data
public class TranslatedArticle implements Serializable {
    private long id;
    private ArticleType type;
    private String title;
    private String description;
    private String text;
    private List<String> categories;
    private String video;
    private Map<Long, TranslatedQuestion> questions;
    private long creationDate;
    private long releaseDate;
    private long calendarStartDate;
    private long calendarFinishDate;
    private long expirationDate;
    private long lastUpdateDate;
}
