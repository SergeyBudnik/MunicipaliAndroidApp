package acropollis.municipali.bootstrap_adapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import acropollis.municipali.bootstrap.data.MunicipaliLoadableIconData;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipalidata.dto.article.question.answer.TranslatedAnswer;
import acropollis.municipali.rest.wrappers.RestListener;
import acropollis.municipali.rest.wrappers.omega.ArticlesRestWrapper;
import acropollis.municipali.service.ArticlesService;

@EBean
public class AnswerBootstrapAdapter {
    @Bean
    ArticlesRestWrapper articlesRestWrapper;
    @Bean
    ArticlesService articlesService;

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
                return articlesService.getAnswerIcon(answer.getId());
            }
        };
    }

    private MunicipaliLoadableIconData.IconFromNetworkLoader getAnswerIconLoader(final long articleId, final long questionId, final TranslatedAnswer answer) {
        return new MunicipaliLoadableIconData.IconFromNetworkLoader() {
            @Override
            public void load(final MunicipaliLoadableIconData.IconLoadingListener listener) {
                articlesRestWrapper.loadAnswerIcon(articleId, questionId, answer.getId(), new RestListener<byte []>() {
                    @Override
                    public void onSuccess(byte [] icon) {
                        articlesService.saveAnswerIcon(answer.getId(), icon);

                        listener.onSuccess(icon);
                    }

                    @Override
                    public void onFailure() {
                        listener.onFailure();
                    }
                });
            }
        };
    }
}
