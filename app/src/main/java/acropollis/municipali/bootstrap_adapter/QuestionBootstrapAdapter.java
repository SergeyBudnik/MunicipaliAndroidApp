package acropollis.municipali.bootstrap_adapter;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import acropollis.municipali.bootstrap.data.MunicipaliLoadableIconData;
import acropollis.municipali.bootstrap.data.MunicipaliRowData;
import acropollis.municipali.data.AnswerStatus;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.dto.article.question.Question;
import acropollis.municipalidata.dto.article.question.TranslatedQuestion;
import acropollis.municipali.service.UserAnswerService;

@EBean
public class QuestionBootstrapAdapter {
    @Bean
    UserAnswerService userAnswerService;

    public List<MunicipaliRowData> getQuestionsRows(TranslatedArticle article) {
        List<MunicipaliRowData> municipaliRowDataList = new ArrayList<>();

        for (TranslatedQuestion question : article.getQuestions().values()) {
            municipaliRowDataList.add(getQuestionRowInfo(article, question));
        }

        return municipaliRowDataList;
    }

    public MunicipaliRowData getQuestionRowInfo(final TranslatedArticle article, final TranslatedQuestion question) {
        return new MunicipaliRowData(
                question.getId(),
                question.getText(),
                new MunicipaliLoadableIconData(
                        question.getId(),
                        null,
                        new MunicipaliLoadableIconData.IconFromNetworkLoader() {
                            @Override
                            public void load(MunicipaliLoadableIconData.IconLoadingListener listener) {
                                listener.onFailure();
                            }
                        }
                ),
                new MunicipaliRowData.TransparencyInfo() {
                    @Override
                    public boolean isTransparent() {
                        return userAnswerService.getAnswerStatus(article.getId(), question.getId()) != AnswerStatus.UNANSWERED;
                    }
                },
                MunicipaliRowData.NO_COUNTER
        );
    }
}
