package acropollis.municipalidata.dao.answer;

import android.content.Context;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.Locale;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.common.CommonDao;

@EBean(scope = EBean.Scope.Singleton)
public class UserAnswersDao extends CommonDao<UserAnswerModel> {
    @RootContext
    Context context;

    public Optional<Long> getAnswer(ProductConfiguration configuration, long articleId, long questionId) {
        readCache(context, getFileName(configuration), false);

        return Optional.ofNullable(getValue().getAnswers().get(getId(articleId, questionId)));
    }

    public void addAnswer(ProductConfiguration configuration, long articleId, long questionId, long answerId) {
        readCache(context, getFileName(configuration), false);

        getValue().getAnswers().put(getId(articleId, questionId), answerId);

        persist(configuration, context);
    }

    @Override
    protected UserAnswerModel newInstance() {
        return new UserAnswerModel();
    }

    @Override
    protected String getFileName(ProductConfiguration configuration) {
        return UserAnswersDao.class.getCanonicalName() +
                "." +
                configuration.getProductId();
    }

    private String getId(long articleId, long questionId) {
        return String.format(Locale.ENGLISH, "%d_%d", articleId, questionId);
    }
}
