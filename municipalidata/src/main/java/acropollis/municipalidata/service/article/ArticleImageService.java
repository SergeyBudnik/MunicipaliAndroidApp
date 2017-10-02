package acropollis.municipalidata.service.article;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.article.ArticleImageDao;

@EBean
public class ArticleImageService {
    @Bean
    ArticleImageDao articleImageDao;

    public Optional<byte []> getArticleImage(ProductConfiguration configuration, long id) {
        return articleImageDao.getIcon(configuration, id);
    }

    public void saveArticleImage(ProductConfiguration configuration, long id, byte [] icon) {
        articleImageDao.addIcon(configuration, id, icon);
    }
}
