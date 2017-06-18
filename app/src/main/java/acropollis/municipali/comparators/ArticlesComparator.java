package acropollis.municipali.comparators;

import java.util.Comparator;

import acropollis.municipali.data.article.Article;

public class ArticlesComparator implements Comparator<Article> {
    @Override
    public int compare(Article a1, Article a2) {
        if (a1.getLastUpdateDate() > a2.getLastUpdateDate()) {
            return -1;
        } else if (a1.getLastUpdateDate() < a2.getLastUpdateDate()) {
            return 1;
        } else {
            return 0;
        }
    }
}
