package xyz.yunzhongyan.www.dao.mongo.bbs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.yunzhongyan.www.domain.po.bbs.Comment;
import xyz.yunzhongyan.www.domain.po.bbs.Topic;

import java.util.List;


public interface CommentDao extends MongoRepository<Comment, String> {
    Comment findByCommentIdIs(String id);

    Comment findFirstByTopicIdIsOrderByInTimeDesc(String id);

    Page<Comment> findByTopicIdIsAndCommentIdIsNullOrderByInTimeDesc(String id, Pageable pageable);

    List<Comment> findByFistCommentIdIsAndCommentIdIsNotNullOrderByInTimeDesc(String id);
}