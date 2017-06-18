package acropollis.municipali.comparators;

import java.util.Comparator;

import acropollis.municipali.data.article.TranslatedArticle;

public class EventsComparator implements Comparator<TranslatedArticle> {
    @Override
    public int compare(TranslatedArticle e1, TranslatedArticle e2) {
        if (e1.getExpirationDate() > e2.getExpirationDate()) {
            return 1;
        } else if (e1.getExpirationDate() < e2.getExpirationDate()) {
            return -1;
        } else {
            return 0;
        }
    }
}
