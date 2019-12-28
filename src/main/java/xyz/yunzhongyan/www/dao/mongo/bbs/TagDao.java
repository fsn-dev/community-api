package xyz.yunzhongyan.www.dao.mongo.bbs;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.yunzhongyan.www.domain.po.bbs.Tag;


public interface TagDao extends MongoRepository<Tag, String> {
    Tag findByIdIs(String id);
}