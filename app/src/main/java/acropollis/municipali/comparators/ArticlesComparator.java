package acropollis.municipali.comparators;

import java.util.Comparator;

import acropollis.municipalidata.dto.article.TranslatedArticle;

public class ArticlesComparator implements Comparator<TranslatedArticle> {
    @Override
    public int compare(TranslatedArticle a1, TranslatedArticle a2) {
        int releaseDateCompareRes = compareByReleaseDate(a1, a2);

        return releaseDateCompareRes != 0 ?
                releaseDateCompareRes :
                compareByLastUpdateDate(a1, a2);
    }

    private int compareByReleaseDate(TranslatedArticle a1, TranslatedArticle a2) {
        if (a1.getReleaseDate() > a2.getReleaseDate()) {
            return -1;
        } else if (a1.getReleaseDate() < a2.getReleaseDate()) {
            return 1;
        } else {
            return 0;
        }
    }

    private int compareByLastUpdateDate(TranslatedArticle a1, TranslatedArticle a2) {
        if (a1.getLastUpdateDate() > a2.getLastUpdateDate()) {
            return -1;
        } else if (a1.getLastUpdateDate() < a2.getLastUpdateDate()) {
            return 1;
        } else {
            return 0;
        }
    }
}
