package xyz.yunzhongyan.www.dao.mongo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.yunzhongyan.www.domain.po.Article;

import java.util.Date;
import java.util.List;


public interface ArticleDao extends MongoRepository<Article,String> {
    public List<Article> findAllByArticleContinerLike(String keyWord,Sort sort);
    public Article findArticleByIdIs(String id);

    Page<Article> findByOrderByCreateTimeDesc(Pageable pageable);

    Article findByIdIs(String id);

    Article findFirstByCreateTimeGreaterThanOrderByCreateTimeAsc(Date time);

    Article findFirstByCreateTimeLessThanOrderByCreateTimeDesc(Date time);
}
