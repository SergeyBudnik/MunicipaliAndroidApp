package acropollis.municipali.service;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import acropollis.municipali.comparators.EventsComparator;
import acropollis.municipalidata.dto.article.ArticleType;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipali.utls.DateUtils;
import acropollis.municipalidata.service.article.ArticleService;

@EBean
public class EventsService {
    @Bean
    ProductConfigurationService productConfigurationService;

    @Bean
    ArticleService articlesService;

    @Bean
    DateUtils dateUtils;

    public List<TranslatedArticle> getTodayEvents() {
        List<TranslatedArticle> todayEvents = new ArrayList<>();

        Date currentDate = new Date();

        for (TranslatedArticle article : articlesService.getArticles(productConfigurationService.getProductConfiguration())) {
            if (article.getType() == ArticleType.EVENT) {
                Date expirationDate = new Date(article.getExpirationDate());

                boolean yearMatches = getYear(currentDate) == getYear(expirationDate);
                boolean monthMatches = getMonth(currentDate) == getMonth(expirationDate);
                boolean dateMatches = getDate(currentDate) == getDate(expirationDate);

                if (yearMatches && monthMatches && dateMatches) {
                    todayEvents.add(article);
                }
            }
        }

        Collections.sort(todayEvents, new EventsComparator());

        return todayEvents;
    }

    public List<TranslatedArticle> getSoonEvents() {
        List<TranslatedArticle> soonEvents = new ArrayList<>();

        Date currentDate = new Date();

        for (TranslatedArticle article : articlesService.getArticles(productConfigurationService.getProductConfiguration())) {
            if (article.getType() == ArticleType.EVENT) {
                Date expirationDate = new Date(article.getExpirationDate());

                int distance = dateUtils.getDistanceInDays(currentDate, expirationDate);

                if (0 < distance && distance <= Calendar.SATURDAY) {
                    soonEvents.add(article);
                }
            }
        }

        Collections.sort(soonEvents, new EventsComparator());

        return soonEvents;
    }

    public List<TranslatedArticle> getThisMonthEvents() {
        List<TranslatedArticle> soonEvents = new ArrayList<>();

        Date currentDate = new Date();

        for (TranslatedArticle article : articlesService.getArticles(productConfigurationService.getProductConfiguration())) {
            if (article.getType() == ArticleType.EVENT) {
                Date expirationDate = new Date(article.getExpirationDate());

                boolean yearMatches = getYear(currentDate) == getYear(expirationDate);
                boolean monthMatches = getMonth(currentDate) == getMonth(expirationDate);
                boolean dateMatches = getDate(currentDate) <= getDate(expirationDate);

                if (yearMatches && monthMatches && dateMatches) {
                    soonEvents.add(article);
                }
            }
        }

        return soonEvents;
    }

    private int getDate(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    private int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal.get(Calendar.MONTH);
    }

    private int getYear(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        return cal.get(Calendar.YEAR);
    }
}
