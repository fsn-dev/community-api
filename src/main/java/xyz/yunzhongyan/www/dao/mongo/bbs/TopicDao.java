package xyz.yunzhongyan.www.dao.mongo.bbs;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.yunzhongyan.www.domain.po.bbs.Topic;

import java.util.List;


public interface TopicDao extends MongoRepository<Topic, String> {
    Topic findByIdIs(String id);
    Topic findFirstByTagIdIsOrderByCreatTimeDesc(String id);
    Page<Topic> findByUserIdIsOrderByCreatTimeDesc(String userId, Pageable pageable);
    Page<Topic> findByTagIdIsOrderByCreatTimeDesc(String tagId, Pageable pageable);
    Page<Topic> findByTagIdIsOrderByHootValueDesc(String tagId, Pageable pageable);


    Page<Topic> findByOrderByCreatTimeDesc(Pageable pageable);
    Page<Topic> findByOrderByHootValueDesc(Pageable pageable);
}