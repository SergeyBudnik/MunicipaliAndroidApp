package acropollis.municipali.dao;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import acropollis.municipali.service.ProductConfigurationService;
import lombok.Data;

@Data
class UserAnswersData implements Serializable {
    private Map<String, Long> answers = new HashMap<>();
}

@EBean(scope = EBean.Scope.Singleton)
public class UserAnswersDao extends CommonDao<UserAnswersData> {
    @RootContext
    Context context;

    @Bean
    ProductConfigurationService productConfigurationService;

    public Long getAnswer(long articleId, long questionId) {
        readCache(context, getFileName(), false);

        return getValue().getAnswers().get(getId(articleId, questionId));
    }

    public void addAnswer(long articleId, long questionId, long answerId) {
        readCache(context, getFileName(), false);

        getValue().getAnswers().put(getId(articleId, questionId), answerId);

        persist(context);
    }

    @Override
    protected UserAnswersData newInstance() {
        return new UserAnswersData();
    }

    @Override
    protected String getFileName() {
        return UserAnswersDao.class.getCanonicalName() +
                "." +
                productConfigurationService.getProductConfiguration().getProductId();
    }

    private String getId(long articleId, long questionId) {
        return String.format("%d_%d", articleId, questionId);
    }
}
