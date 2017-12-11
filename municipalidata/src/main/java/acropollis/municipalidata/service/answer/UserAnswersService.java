package acropollis.municipalidata.service.answer;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.answer.UserAnswersDao;
import acropollis.municipalidata.dto.answer.AnswerStatus;
import acropollis.municipalidata.dto.article.TranslatedArticle;
import acropollis.municipalidata.service.article.ArticleService;

@EBean
public class UserAnswersService {
    @Bean
    UserAnswersDao userAnswersDao;
    @Bean
    ArticleService articleService;

    public int getUnansweredQuestionsAmount(ProductConfiguration configuration, long articleId) {
        TranslatedArticle article = articleService
                .getArticle(configuration, articleId)
                .orElseThrow(RuntimeException::new);

        int amount = 0;

        for (long questionId : article.getQuestions().keySet()) {
            if (getAnswerStatus(configuration, articleId, questionId) == AnswerStatus.UNANSWERED) {
                amount++;
            }
        }

        return amount;
    }

    public AnswerStatus getAnswerStatus(ProductConfiguration configuration, long articleId, long questionId) {
        Optional<Long> answer = userAnswersDao.getAnswer(configuration, articleId, questionId);

        if (!answer.isPresent()) {
            return AnswerStatus.UNANSWERED;
        } else if (answer.get() == 0) {
            return AnswerStatus.SKIPPED;
        } else {
            return AnswerStatus.ANSWERED;
        }
    }

    public Optional<Long> getAnswer(ProductConfiguration configuration, long articleId, long questionId) {
        return userAnswersDao.getAnswer(configuration, articleId, questionId);
    }

    public void addAnswer(ProductConfiguration configuration, long articleId, long questionId, long answerId) {
        userAnswersDao.addAnswer(configuration, articleId, questionId, answerId);
    }
}
