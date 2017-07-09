package acropollis.municipali.comparators;

import java.util.Comparator;

import acropollis.municipali.data.article.TranslatedArticle;

public class ArticlesComparator implements Comparator<TranslatedArticle> {
    @Override
    public int compare(TranslatedArticle a1, TranslatedArticle a2) {
        if (a1.getReleaseDate() > a2.getReleaseDate()) {
            return -1;
        } else if (a1.getReleaseDate() < a2.getReleaseDate()) {
            return 1;
        } else {
            return 0;
        }
    }
}
