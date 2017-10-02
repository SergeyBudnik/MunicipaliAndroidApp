package acropollis.municipali.bootstrap_adapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import acropollis.municipali.bootstrap.data.MunicipaliLoadableIconData;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.data.AnswerStatus;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.UserAnswerService;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.service.article.ArticleImageService;

@EBean
public class ArticleBootstrapAdapter {
    @Bean
    ArticleImageService articlesService;
    @Bean
    UserAnswerService userAnswerService;
    @Bean
    ProductConfigurationService productConfigurationService;

    @Bean
    ArticleRestWrapper articlesRestWrapper;

    public List<MunicipaliRowData> getArticlesRows(List<TranslatedArticle> articles) {
        List<MunicipaliRowData> municipaliRowDataList = new ArrayList<>();

        for (TranslatedArticle article : articles) {
            municipaliRowDataList.add(getArticleRowInfo(article, true));
        }

        return municipaliRowDataList;
    }


    public MunicipaliRowData getArticleRowInfo(TranslatedArticle article, boolean showCounter) {
        return new MunicipaliRowData(
                article.getId(),
                article.getTitle(),
                getArticleRowIconInfo(article),
                MunicipaliRowData.NO_TRANSPARENCY,
                showCounter ? getCounterInfo(article) : MunicipaliRowData.NO_COUNTER
        );
    }

    private MunicipaliLoadableIconData getArticleRowIconInfo(TranslatedArticle article) {
        return new MunicipaliLoadableIconData(
                article.getId(),
                getArticleIconFromCacheLoader(article),
                getArticleIconLoader(article)
        );
    }

    private MunicipaliLoadableIconData.IconFromCacheLoader getArticleIconFromCacheLoader(final TranslatedArticle article) {
        return new MunicipaliLoadableIconData.IconFromCacheLoader() {
            @Override
            public byte[] load() {
                return articlesService.getArticleImage(
                        productConfigurationService.getProductConfiguration(),
                        article.getId()
                ).orElse(null);
            }
        };
    }

    private MunicipaliLoadableIconData.IconFromNetworkLoader getArticleIconLoader(final TranslatedArticle article) {
//        return new MunicipaliLoadableIconData.IconFromNetworkLoader() {
//            @Override
//            public void load(final MunicipaliLoadableIconData.IconLoadingListener listener) {
//                articlesRestWrapper.loadArticleImage(article.getId(), new RestListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte [] icon) {
//                        articlesService.saveArticleImage(
//                                productConfigurationService.getProductConfiguration(),
//                                article.getId(), icon);
//
//                        listener.onSuccess(icon);
//                    }
//
//                    @Override
//                    public void onFailure() {
//                        listener.onFailure();
//                    }
//                });
//            }
//        };

        return null;
    }

    private MunicipaliRowData.CounterInfo getCounterInfo(final TranslatedArticle article) {
        return new MunicipaliRowData.CounterInfo() {
            @Override
            public int getCounter() {
                int unansweredQuestions = 0;

                for (TranslatedQuestion question : article.getQuestions().values()) {
                    if (userAnswerService.getAnswerStatus(article.getId(), question.getId()) == AnswerStatus.UNANSWERED) {
                        unansweredQuestions++;
                    }
                }

                return unansweredQuestions;
            }
        };
    }
}
