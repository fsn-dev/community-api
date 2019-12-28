package xyz.yunzhongyan.www.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import xyz.yunzhongyan.www.domain.po.UserIntegralRecord;

import java.util.Date;


public interface UserIntergralRecordDao extends MongoRepository<UserIntegralRecord, String> {
    UserIntegralRecord findByUserIdIsAndTimeIsAndTypeIs(String id, Date time,Integer tyep);
}
