package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.data.UserAnswer;
import lombok.Data;

@Data
class UserAnswersData implements Serializable {
    private Map<String, Long> answers = new HashMap<>();
}

@EBean(scope = EBean.Scope.Singleton)
public class UserAnswersDao extends CommonDao<UserAnswersData> {
    private static final String FILE_NAME = UserAnswersDao.class.getCanonicalName();

    @RootContext
    Context context;

    public Long getAnswer(long articleId, long questionId) {
        readCache(context, FILE_NAME, false);

        return cache.getAnswers().get(getId(articleId, questionId));
    }

    public void addAnswer(long articleId, long questionId, long answerId) {
        readCache(context, FILE_NAME, false);

        cache.getAnswers().put(getId(articleId, questionId), answerId);
    }

    @Override
    protected UserAnswersData newInstance() {
        return new UserAnswersData();
    }

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    private String getId(long articleId, long questionId) {
        return String.format("%d_%d", articleId, questionId);
    }
}
