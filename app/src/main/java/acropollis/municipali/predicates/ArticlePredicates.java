package acropollis.municipali.predicates;

import com.annimon.stream.function.Predicate;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.Date;

import acropollis.municipali.utls.DateUtils;
import acropollis.municipalidata.dto.article.TranslatedArticle;

@EBean
public class ArticlePredicates {
    @Bean
    DateUtils dateUtils;

    public Predicate<TranslatedArticle> articleReleaseDateMatch(Date startDate, Date endDate) {
        Predicate<TranslatedArticle> startDateMatches = it -> {
            if (startDate == null) {
                return true;
            }

            Date formattedArticleDate = dateUtils.formatDate(new Date(it.getReleaseDate()));

            return startDate.getTime() <= formattedArticleDate.getTime();
        };

        Predicate<TranslatedArticle> endDateMatches = it -> {
            if (startDate == null) {
                return true;
            }

            Date formattedArticleDate = dateUtils.formatDate(new Date(it.getReleaseDate()));

            if (endDate == null) {
                return formattedArticleDate.getTime() <= startDate.getTime();
            } else {
                return formattedArticleDate.getTime() <= endDate.getTime();
            }
        };

        return it -> startDateMatches.test(it) && endDateMatches.test(it);
    }
}
