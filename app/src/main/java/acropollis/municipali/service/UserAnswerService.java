package acropollis.municipali.service;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipali.dao.UserAnswersDao;
import acropollis.municipali.data.AnswerStatus;

@EBean
public class UserAnswerService {
    @Bean
    UserAnswersDao userAnswersDao;

    public AnswerStatus getAnswerStatus(long articleId, long questionId) {
        Long answer = userAnswersDao.getAnswer(articleId, questionId);

        if (answer == null) {
            return AnswerStatus.UNANSWERED;
        } else if (answer == 0) {
            return AnswerStatus.SKIPPED;
        } else {
            return AnswerStatus.ANSWERED;
        }
    }

    public Long getAnswer(long articleId, long questionId) {
        return userAnswersDao.getAnswer(articleId, questionId);
    }

    public void addAnswer(long articleId, long questionId, long answerId) {
        userAnswersDao.addAnswer(articleId, questionId, answerId);
    }
}
