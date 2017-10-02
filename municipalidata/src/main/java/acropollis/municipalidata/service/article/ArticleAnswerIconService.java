package acropollis.municipalidata.service.article;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.article.ArticleAnswerIconDao;

@EBean
public class ArticleAnswerIconService {
    @Bean
    ArticleAnswerIconDao articleAnswerIconDao;

    public Optional<byte []> getAnswerIcon(ProductConfiguration configuration, long id) {
        return articleAnswerIconDao.getIcon(configuration, id);
    }

    public void saveAnswerIcon(ProductConfiguration configuration, long id, byte [] icon) {
        articleAnswerIconDao.addIcon(configuration, id, icon);
    }
}
