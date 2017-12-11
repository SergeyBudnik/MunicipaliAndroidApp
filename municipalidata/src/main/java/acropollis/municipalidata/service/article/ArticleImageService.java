package acropollis.municipalidata.service.article;

import com.annimon.stream.Optional;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import acropollis.municipalidata.configuration.ProductConfiguration;
import acropollis.municipalidata.dao.article.ArticleClippedImageDao;
import acropollis.municipalidata.dao.article.ArticleImageDao;

@EBean
public class ArticleImageService {
    @Bean
    ArticleImageDao articleImageDao;
    @Bean
    ArticleClippedImageDao articleClippedImageDao;

    public Optional<byte []> getArticleImage(ProductConfiguration configuration, long id) {
        return articleImageDao.getIcon(configuration, id);
    }

    public void saveArticleImage(ProductConfiguration configuration, long id, byte [] icon) {
        articleImageDao.addIcon(configuration, id, icon);
    }

    public Optional<byte []> getClippedArticleImage(ProductConfiguration configuration, long id) {
        return articleClippedImageDao.getIcon(configuration, id);
    }

    public void saveArticleClippedImage(ProductConfiguration configuration, long id, byte [] icon) {
        articleClippedImageDao.addIcon(configuration, id, icon);
    }
}
