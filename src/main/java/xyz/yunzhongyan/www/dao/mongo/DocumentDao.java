package xyz.yunzhongyan.www.dao.mongo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.yunzhongyan.www.domain.po.Article;
import xyz.yunzhongyan.www.domain.po.Document;


public interface DocumentDao extends MongoRepository<Document,String> {
    Page<Document> findByOrderByIdDesc(Pageable pageable);
}
