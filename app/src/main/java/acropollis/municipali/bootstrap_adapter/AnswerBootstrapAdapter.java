package acropollis.municipali.bootstrap_adapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import acropollis.municipali.bootstrap.data.MunicipaliLoadableIconData;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.service.ProductConfigurationService;
import acropollis.municipalidata.dto.article.question.answer.TranslatedAnswer;
import acropollis.municipalidata.rest_wrapper.article.ArticleRestWrapper;
import acropollis.municipalidata.service.article.ArticleAnswerIconService;

@EBean
public class AnswerBootstrapAdapter {
    @Bean
    ArticleRestWrapper articlesRestWrapper;
    @Bean
    ArticleAnswerIconService articleAnswerIconService;
    @Bean
    ProductConfigurationService productConfigurationService;

    public List<MunicipaliRowData> getAnswersRows(long articleId, long questionId, List<TranslatedAnswer> answers) {
        List<MunicipaliRowData> municipaliRowDataList = new ArrayList<>();

        for (TranslatedAnswer answer : answers) {
            municipaliRowDataList.add(getAnswerRowInfo(articleId, questionId, answer));
        }

        return municipaliRowDataList;
    }

    private MunicipaliRowData getAnswerRowInfo(long articleId, long questionId, TranslatedAnswer answer) {
        return new MunicipaliRowData(
                answer.getId(),
                answer.getText(),
                new MunicipaliLoadableIconData(
                        answer.getId(),
                        getAnswerIconCacheLoader(answer),
                        getAnswerIconLoader(articleId, questionId, answer)
                ),
                MunicipaliRowData.NO_TRANSPARENCY,
                MunicipaliRowData.NO_COUNTER
        );
    }

    private MunicipaliLoadableIconData.IconFromCacheLoader getAnswerIconCacheLoader(final TranslatedAnswer answer) {
        return new MunicipaliLoadableIconData.IconFromCacheLoader() {
            @Override
            public byte[] load() {
                return articleAnswerIconService.getAnswerIcon(
                        productConfigurationService.getProductConfiguration(),
                        answer.getId()
                ).orElse(null);
            }
        };
    }

    private MunicipaliLoadableIconData.IconFromNetworkLoader getAnswerIconLoader(final long articleId, final long questionId, final TranslatedAnswer answer) {
//        return new MunicipaliLoadableIconData.IconFromNetworkLoader() {
//            @Override
//            public void load(final MunicipaliLoadableIconData.IconLoadingListener listener) {
//                articlesRestWrapper.loadAnswerIcon(articleId, questionId, answer.getId(), new RestListener<byte []>() {
//                    @Override
//                    public void onSuccess(byte [] icon) {
//                        articleAnswerIconService.saveAnswerIcon(productConfigurationService.getProductConfiguration(), answer.getId(), icon);
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
}
