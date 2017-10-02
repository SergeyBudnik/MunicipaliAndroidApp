package acropollis.municipalidata.dao.article;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipalidata.dto.article.Article;
import lombok.Data;

@Data
class ArticleModel implements Serializable {
    private Map<Long, Article> allArticles = new HashMap<>();
}
